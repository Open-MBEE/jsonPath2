package gov.nasa.jpl.jsonPath2;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import gov.nasa.jpl.jsonPath2.JsonPath2Parser.*;

import static gov.nasa.jpl.jsonPath2.Utils.*;

/**
 * Represents a query against a JSON object
 * 
 * @author David K. Legg
 *
 */
public class Path implements Comparable<Path> {
  /// Private constants
  
  private static final List<String> HERE_TAGS      = Arrays.asList("HERE", "HERE:", "~"); // declare the canonical form first, by convention
  private static final List<String> ROOT_TAGS      = Arrays.asList("DOC", "DOC:", "$");
  private static final List<String> REFERENCE_TAGS = Arrays.asList("REF", "REF:", "^");
  private static final List<String> LIBRARY_TAGS   = Arrays.asList("LIB", "LIB:");
  private static final List<String> PARENT_TAGS    = Arrays.asList(".^");
  private static final List<String> WILD_TAGS      = Arrays.asList(".*");
  private static final String HERE_TAG      = "HERE"; // make sure the canonical form here matches the first element of the corresponding list above
  private static final String ROOT_TAG      = "DOC";
  private static final String REFERENCE_TAG = "REF";
  private static final String LIBRARY_TAG   = "LIB";
  private static final String PARENT_TAG    = ".^";
  private static final String WILD_TAG      = ".*";
  private static final String RECURSIVE_ELEM = ".**";
  private static final String RECURSIVE_UP_ELEM = ".^^";
  private static final String WILD_INDEX = "*";
  
  private static final Set<PathElement> constantElements = makeConstantElements();
  private static Set<PathElement> makeConstantElements() {
    return new LinkedHashSet<>(Arrays.asList(
        new TagPathElement(ROOT_TAG),
        new TagPathElement(REFERENCE_TAG),
        new TagPathElement(LIBRARY_TAG) ));
  }
  
  /// Private members

  private static Map<Path, AccessCache> staticAccessCache = Collections.synchronizedMap(new LinkedHashMap<>());
  
  private static Integer mainCacheHits      = 0;
  private static Integer mainCacheMisses    = 0;
  private static Logger  cacheReportLogger  = null;
  private static Integer cacheReportPeriod  = 1000000; // number of queries between cache reports
  private static Integer queriesSinceCacheReport = 0;
  
  private PathElement element;
  private List<Path> branches;
  
  private Integer hashCode = null;
  private Boolean shouldCache = false;
  
  /// Public methods
  
  /**
   * A routine for testing Paths, and running individual queries against a model.
   * 
   * Flags:<ul>
   *   <li>--path, -p <pathString> : specify the path to use. Necessary.</li>
   *   <li>--model, --file, --modelFile, -m, -f <filePath> : specify the file against which to run a query. Optional.</li>
   *   <li>--echo, -e : echo the path back after parsing it, to check that path was correctly parsed.</li>
   * </ul>
   */
  public static void main(String[] args) {
    try {
      List<String> pathOptions  = Arrays.asList("path", "p");
      List<String> modelOptions = Arrays.asList("model", "modelFile", "file", "m", "f");
      List<List<String>> optionLists = Arrays.asList(pathOptions, modelOptions);
      
      List<String> helpFlags    = Arrays.asList("help", "h");
      List<String> echoFlags    = Arrays.asList("echo", "e", "verbose", "v");
      List<String> quietFlags   = Arrays.asList("quiet", "q");
      List<List<String>> flagLists = Arrays.asList(helpFlags, echoFlags, quietFlags);
      
      Map<String,String> argMap = optionReader(optionLists, flagLists).apply(args);
      
      Function<List<String>, Optional<String>> getOption = keys -> Optional.ofNullable( argMap.get(keys.get(0)) );
      Function<List<String>, Optional<Boolean>> getFlag  = keys -> getOption.apply(keys).map( "true"::equalsIgnoreCase );

      final Integer lengthLimit = 100;
      Function<Object, String> limitLength = x -> {
        String s = x.toString();
        if (s.length() > lengthLimit) {
          s = s.substring(0, lengthLimit - 3) + "...";
        }
        return s;
      };
      
      Logger logger = new Logger();
      
      if (getFlag.apply(helpFlags).orElse(false)) {
        // if you ask for help, just construct the help message and quit
        logger.log("Options:");
        for (List<String> longOptionList : optionLists) {
          logger.log(longOptionList.stream().collect( Collectors.joining(", ") ));
        }
        
        logger.log("Flags:");
        for(List<String> flagList : flagLists) {
          logger.log(flagList.stream().map(s -> s + "[Off]").collect( Collectors.joining(", ") ));
        }
        return;
      }

      if (getFlag.apply(quietFlags).orElse(false)) {
        logger.setMaxVerbosity(Logger.VERBOSITY.QUIET);
      }
      if (getFlag.apply(echoFlags).orElse(false)) {
        logger.setMinVerbosity(Logger.VERBOSITY.VERBOSE);
      }
      
      Optional<JsonNode> modelOpt = getOption.apply(modelOptions)
          .flatMap(optOnError( str -> new FileInputStream(str) ))
          .map( Utils::streamToString )
          .map( JsonNode::readFrom );
      
      if (modelOpt.isPresent()) {
        logger.verbose("Using model: %s", limitLength.apply(modelOpt.get()));
      } else {
        logger.warning("No model found.");
      }
      
      Optional<String> pathStrOpt = getOption.apply(pathOptions);
      
      Consumer<Collection<JsonNode>> printResults = res -> {
        if (!res.isEmpty()) {
          logger.log("Query successful. %d matches.", res.size());
          Integer i = 0;
          for (JsonNode node : res) {
            ++i;
            if (i > 5) {
              logger.log("...");
              break;
            }
            logger.log("%s: %s", Logger.VERBOSITY.QUIET, node.getClass(), limitLength.apply(node));
          }
          
        } else {
          logger.log("Query failed. (no results)");
        }
      };
      
      if (pathStrOpt.isPresent()) {
        // non-interactive mode
        Path path = Path.fromPathStr(pathStrOpt.get());
        
        logger.verbose("Read path: %s", path);
        
        if (modelOpt.isPresent()) {
          printResults.accept( path.access(modelOpt.get(), Collections.emptyList()) );
        }
        
      } else {
        // interactive mode
        try (Scanner systemInScanner = new Scanner(System.in)) {
          String pathStr = "";
          String line;
          JsonNode reference = new ObjectNode();
          Collection<JsonNode> results;
          while (true) {
            logger.verbose("Reference object: %s", limitLength.apply(reference));
            logger.log("Input path:");
            line = systemInScanner.nextLine();
            while (!line.isEmpty()) {
              pathStr += line;
              line = systemInScanner.nextLine();
            }
            pathStr = pathStr.trim();
            if (pathStr.isEmpty()) break;
            //else:
            try {
              Path path = Path.fromPathStr(StringEscapeUtils.unescapeJava(pathStr));
              logger.verbose("Read path: %s", path);
              if (modelOpt.isPresent()) {
                results = path.access(modelOpt.get(), reference, Collections.emptyList());
                printResults.accept(results);
                if (!results.isEmpty()) {
                  reference = results.iterator().next();
                }
              }
            } catch (IllegalArgumentException e) {
              logger.error("Path parse error: %s", e);
            }
            
            pathStr = "";
          }
        }
      }
      
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Constructs a Path representing accessing the root of the object
   * @see {@link Path#Path(Integer)}
   * @see {@link Path#Path(String)}
   * @see {@link Path#Path(String, String, Path)}
   * @see {@link Path#fromPathStr(String)}
   */
  public Path() {
    this(new TagPathElement());
  }
  /**
   * Constructs a Path representing attribute access
   * @param tag The attribute to access
   * @see {@link Path#Path()}
   * @see {@link Path#Path(Integer)}
   * @see {@link Path#Path(String, String, Path)}
   * @see {@link Path#fromPathStr(String)}
   */
  public Path(String tag) {
    this(new TagPathElement(tag));
  }
  /**
   * Constructs a Path representing index access
   * @param index The array index to access, by 0-based indexing
   * @see {@link Path#Path()}
   * @see {@link Path#Path(String)}
   * @see {@link Path#Path(String, String, Path)}
   * @see {@link Path#fromPathStr(String)}
   */
  public Path(Integer index) {
    this(new IndexPathElement(index));
  }
  /**
   * Constructs a Path representing a filter by the value of an attribute
   * @param attribute The attribute of an object to compare to value
   * @param value The value that attribute must have for the filter to succeed
   * @param modifies The Path that this filter is modifying. Inserts the filter between modifies and its branches.
   * @see {@link Path#Path()}
   * @see {@link Path#Path(Integer)}
   * @see {@link Path#Path(String)}
   * @see {@link Path#fromPathStr(String)}
   */
  public Path(String attribute, String value, Path modifies) {
    this(modifies.element);
    Path branch = new Path(new AttributeFilterPathElement(new Path(new TagPathElement(attribute)), value));
    branch.branches = modifies.branches;
    this.branches.add(branch);
  }
  
  /**
   * Constructs a Path based on the textual representation
   * @param pathStr The textual representation of a Path, as given by {@link Path#toString()}
   * @return The Path specified by the path string
   * @throws S2KParseException if the string cannot be parsed as a Path
   * @see {@link Path#Path()}
   * @see {@link Path#Path(Integer)}
   * @see {@link Path#Path(String)}
   * @see {@link Path#Path(String, String, Path)}
   */
  public static Path fromPathStr(String pathStr) throws IllegalArgumentException {
    try {
      JsonPath2Lexer lexer = new JsonPath2Lexer( CharStreams.fromString(pathStr) );
      JsonPath2Parser parser = new JsonPath2Parser( new CommonTokenStream(lexer) );
      PathVisitor pathVisitor = new PathVisitor();
      return pathVisitor.visit(parser.path()); 
    } catch (Exception e) {
      throw new IllegalArgumentException("Could not parse string as a Path.", e);
    }
  }
  
  /**
   * Tests whether this Path is a terminal node
   * @return True if and only if this Path has no branches
   */
  public boolean isLeaf() {
    return branches.size() == 0;
  }
  
  /**
   * Deeply copies this path
   * @return A deep copy of this path and all its branches
   */
  public Path copy() {
    Path output = new Path();
    output.element = this.element.copy();
    output.branches = this.branches.stream()
        .map( path -> path.copy() )
        .collect(Collectors.toList());
    return output;
  }
  
  /**
   * Adds a Path as a branch of this Path, modifying this Path.
   * If the Path is locked, returns without doing so.
   * @param branch The branch to be added
   */
  public void addBranch(Path branch) {
    this.branches.add(branch);
  }
  
  /**
   * Retrieves the values specified by this Path, treating the model as the reference value.
   * For each value, the exact Path, with wildcards replaced by precise values, is returned as well.
   * @param jsonObj The source model to access
   * @param libraries The collection of library objects that can be accessed by library tags
   * @return The values specified by this path, keyed by the exact Paths used to access each value
   * @see {@link Path#access(Object, Object, Collection)}
   */
  public Collection<JsonNode> access(JsonNode jsonObj, Collection<JsonNode> libraries) {
    return access(jsonObj, jsonObj, libraries);
  }
  /**
   * Retrieves the values specified by this Path.
   * For each value, the exact Path, with wildcards replaced by precise values, is returned as well.
   * @param jsonObj The source model to access
   * @param referenceJsonObj The reference object that can be accessed by reference tags
   * @param libraries The collection of library objects that can be accessed by library tags
   * @return The values specified by this path, keyed by the exact Paths used to access each value
   * @see {@link Path#access(Object, Collection)}
   */
  public Collection<JsonNode> access(JsonNode jsonObj, JsonNode referenceJsonObj, Collection<JsonNode> libraries) {
    computeShouldCache(false);
    AccessResult output = innerAccess(new AccessContext(jsonObj, jsonObj, referenceJsonObj, libraries));
    return output;
  }
  
  /**
   * Orders Paths lexically.
   * Specifically, the following rules are always obeyed:
   * <ul>
   *   <li>Earlier nodes take precedence over later nodes</li>
   *   <li>Earlier branches take precedence over later branches</li>
   *   <li>Tags are ordered alphabetically</li>
   *   <li>Indices are ordered numerically</li>
   *   <li>A Path with more branches than, but otherwise identical to, another Path, will come after that other Path</li>
   * <ul>
   * Tags of disparate types have a deterministic, but arbitrary, ordering that may change in future implementations.
   * @param other The Path to be compared to
   */
  @Override
  public int compareTo(Path other) {
    if (this.element instanceof FilterPathElement) {
      // skip a filter element for the purposes of ordering paths
      return this.branches.stream()
          .map( other::compareTo ) // maintain the order of "compareTo's"
          .map( x -> -x ) // negate to compensate for flipped order
          .filter( c -> c != 0 )
          .findFirst()
          .orElse( 0 ); // can only reach here if we have 1 branch, which is exactly like other
      
    } else if (other.element instanceof FilterPathElement) {
      return -other.compareTo(this); // let the above code handle it, but negate the result
      
    } else {
      int elementComp = this.element.compareTo(other.element);
      if (elementComp != 0) {
        return elementComp;
      } // else:
      
      Iterator<Path> thisIt  = this.branches.iterator();
      Iterator<Path> otherIt = other.branches.iterator();
      while (thisIt.hasNext() && otherIt.hasNext()) {
        int branchComp = thisIt.next().compareTo(otherIt.next());
        if (branchComp != 0) {
          return branchComp;
        }
      }
      
      if (thisIt.hasNext()) {
        return 1; // this path is "longer" (has more branches)
      } else if (otherIt.hasNext()) {
        return -1; // this path is "shorter"
      } else {
        return 0; // they're exactly the same
      }
    }
  }
  
  /**
   * Builds the textual representation of this Path.
   * This format can be parsed by {@link Path#fromPathStr(String)}.
   * @return The precise textual representation of this Path
   */
  public String toString() {
    String output = element.toString();
    switch (branches.size()) {
      case 0:
        break;
        
      case 1:
        output += branches.get(0).toString();
        break;
        
      default:
        output += "[" + branches.stream()
                         .map( b -> b.toString() )
                         .collect( Collectors.joining(",") ) + "]";
        break;
    }
    return output;
  }

  /**
   * Converts this Path to a JSON object
   * @return a String in JsonPath2 that represents this path
   * @see {@link Path#fromJSON(Object)}
   */
  public Object toJSON() {
    return this.toString();
  }

  /**
   * Constructs a Path based on the given JSON object, which should be a String in JsonPath2 format.
   * @param jsonObj The JSON object to convert
   * @return The Path specified by jsonObj when parsed as JsonPath2
   * @throws S2KParseException if jsonObj is not a String or fails to parse
   * @see {@link Path#toJSON()}
   */
  public static Path fromJSON(Object jsonObj) throws IllegalArgumentException {
    if (jsonObj instanceof String) {
      return Path.fromPathStr((String) jsonObj);
    } else {
      throw new IllegalArgumentException("Unknown type, could not parse Path.");
    }
  }
  
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    
    if (!(other instanceof Path)) return false;
    
    Path otherPath = (Path) other;
    if (!this.element.equals(otherPath.element) ||
        this.branches.size() != otherPath.branches.size()) return false;
    
    Set<Path> otherBranchesCopy = new LinkedHashSet<>(otherPath.branches);
    otherBranchesCopy.removeAll(this.branches);
    return otherBranchesCopy.isEmpty();
  }
  
  @Override
  public int hashCode() {
    if (hashCode == null) {
      hashCode = new HashCodeBuilder(345, 431)
        .append(element)
        .append(branches)
        .toHashCode();
    }
    return hashCode;
  }
  
  /**
   * Writes a cache report to the logger given for periodical cache reports, if it was set.
   */
  public static void reportCacheUsage() {
    if (cacheReportLogger != null) {
      reportCacheUsage(cacheReportLogger);
    }
  }
  
  /**
   * Writes a report of the cache hits and misses to the DEFAULT/VERBOSE channel on the logger.
   * @param logger The Logger to write the report to.
   */
  public static void reportCacheUsage(Logger logger) {
    Integer cacheNodes = 0;
    for (AccessCache ac : staticAccessCache.values()) {
      cacheNodes += ac.values().size();
    }
    
    logger.verbose("Cache Usage:");
    logger.verbose("  Main Cache Hits       %10d  (%5.2f %% )", mainCacheHits  , 100.0 * mainCacheHits   / (mainCacheHits + mainCacheMisses));
    logger.verbose("  Main Cache Misses     %10d  (%5.2f %% )", mainCacheMisses, 100.0 * mainCacheMisses / (mainCacheHits + mainCacheMisses));
    logger.verbose("  Total Cache Size      %10d keys, %10d nodes", staticAccessCache.size(), cacheNodes);
  }
  
  /**
   * @param logger The Logger to report on the cache with, or null to turn off cache reports.
   */
  public static void setPeriodicalCacheReport(Logger logger) {
    cacheReportLogger = logger;
  }

  /**
   * @param logger The Logger to report on the cache with, or null to turn off cache reports.
   * @param period The number of queries to run between cache reports. Larger numbers -> less frequent reports.
   */
  public static void setPeriodicalCacheReport(Logger logger, Integer period) {
    cacheReportLogger = logger;
    cacheReportPeriod = period;
  }
  
  /// Private helpers

  /**
   * Constructs a new Path with the given element 
   * @param element The PathElement that specifies the behavior this Path at this node
   */
  private Path(PathElement element) {
    this.element = element;
    this.branches = new LinkedList<Path>();
  }

  private void computeShouldCache(Boolean parentIsConstant) {
    shouldCache = parentIsConstant;
    this.element.computeShouldCache(parentIsConstant);
    Boolean amConstant = constantElements.contains(this.element);
    branches.stream().forEach( branch -> branch.computeShouldCache(amConstant) );
  }
  
  /**
   * Actually do the work of accessing a model
   * @param accessContext the relevant information about this stage of the lookup
   * @return A map of exact paths to accessed values, as returned by {@link Path#access}
   * @see @{link {@link Path#innerAccess(AccessContext, Consumer)}
   */
  private AccessResult innerAccess(AccessContext accessContext) {
    AccessCache mainCache = staticAccessCache.computeIfAbsent(this, p -> new AccessCache() );
    AccessResult output   = mainCache.get(accessContext);
    if (output == null) {
      ++mainCacheMisses;
      output = new AccessResult();
      innerAccess(accessContext, output::add);
      mainCache.put(accessContext, output);
    } else {
      ++mainCacheHits;
    }
    // make a copy of the output prior to returning,
    // to insulate the cache against changes to the returned value
    return new AccessResult(output);
  }
  /**
   * Actually do the work of accessing a model
   * @param accessContext the relevant information about this stage of the lookup
   * @param callback The action to take on each accessed node
   * @see @{link {@link Path#innerAccess(AccessContext)}
   */
  private void innerAccess(AccessContext accessContext, Consumer<JsonNode> callback) {
    ++queriesSinceCacheReport;
    if (queriesSinceCacheReport >= cacheReportPeriod) {
      reportCacheUsage();
      queriesSinceCacheReport = 0;
    }

    Consumer<JsonNode> newCallback;
    if (shouldCache) {
      AccessCache accessCache = staticAccessCache.computeIfAbsent(this, path -> new AccessCache());
      AccessResult output = accessCache.get(accessContext);
      if (output != null) {
        ++mainCacheHits;
        output = new AccessResult(output);
        
      } else {
        ++mainCacheMisses;
        AccessResult accessResult = new AccessResult();
        
        if (this.isLeaf()) {
          newCallback = accessResult::add;
        } else {
          newCallback = node ->
            branches.stream().forEach( branch ->
              branch.innerAccess(accessContext.swapObject(node), accessResult::add) );
        }
        
        this.element.access(accessContext, newCallback);
        accessCache.put(accessContext, accessResult);
        staticAccessCache.put(this, accessCache);
        output = accessResult;
        
      }
      
      output.stream().forEach(callback);
      
    } else {
      
      if (this.isLeaf()) {
        newCallback = callback;
      } else {
        newCallback = node -> {
          AccessContext newAC = accessContext.swapObject(node);
          branches.stream().forEach( branch -> branch.innerAccess(newAC, callback) );
        };
      }
      
      this.element.access(accessContext, newCallback);
    }
  }
  
  /// Private inner classes
  
  /**
   * Describes the relevant parameters for accessing a JSON model.
   * 
   * @author David K. Legg
   *
   */
  private static class AccessContext {
    public JsonNode object;
    public JsonNode root;
    public JsonNode reference;
    public Collection<JsonNode> libraries;
    
    private Integer hashCode = null;
    
    /**
     * Group together the relevant aspects of accessing a JSON model
     * @param object The current node in the JSON tree
     * @param root The object accessed by a root tag
     * @param reference The object accessed by a reference tag
     * @param libraries The objects accessed by a library tag
     */
    public AccessContext(JsonNode object, JsonNode root, JsonNode reference, Collection<JsonNode> libraries) {
      this.object    = object;
      this.root      = root;
      this.reference = reference;
      this.libraries = libraries;
    }
    
    /**
     * Performs a shallow copy of this AccessContext
     * @return A new AccessContext with the same values as this one
     */
    public AccessContext copy() {
      return new AccessContext(object, root, reference, libraries);
    }
    
    /**
     * Copies this AccessContext, substituting the given object for the object in this one.
     * Corresponds to "moving" to newObject.
     * @param newObject The new value for the object attribute
     * @return A shallow copy of this AccessContext, with the object attribute set to the specified value
     */
    public AccessContext swapObject(JsonNode newObject) {
      AccessContext output = this.copy();
      output.object = newObject;
      return output;
    }
  
    @Override
    public int hashCode() {
      if (hashCode == null) {
        hashCode = new HashCodeBuilder(91, 75)
            .append(object)
            .append(root)
            .append(reference)
            .append(libraries)
            .toHashCode();
      }
      return hashCode;
    }
    
    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof AccessContext)) return false;
      
      AccessContext accOther = (AccessContext) other;
      
      return (this.object.equals(accOther.object)       &&
              this.root.equals(accOther.root)           &&
              this.reference.equals(accOther.reference) &&
              this.libraries.equals(accOther.libraries) );
    }
  }
  
  /**
   * Allows a path to cache its results efficiently.
   * 
   * @author dlegg
   *
   */
  private class AccessCache extends ConcurrentHashMap<AccessContext, AccessResult> {
    private static final long serialVersionUID = 7480598871339507669L;
  }
  
  /**
   * A specification of path behavior for one Path node.
   * 
   * @author David K. Legg
   *
   */
  private static abstract class PathElement implements Comparable<PathElement> {
    protected static List<Class<? extends PathElement>> childClasses = Arrays.asList(
        TagPathElement.class,
        IndexPathElement.class,
        AlternationPathElement.class,
        RecursivePathElement.class,
        RepeatPathElement.class,
        FilterPathElement.class,
        NegationFilterPathElement.class,
        AttributeFilterPathElement.class,
        RegexFilterPathElement.class,
        DoubleAttributeFilterPathElement.class);

    protected Integer hashCode;
    
    /// Public methods
    
    /**
     * Performs a deep copy of this PathElement
     * @return A deep copy of this PathElement and any internal data
     */
    public abstract PathElement copy();
    /**
     * Operate on the values specified by this PathElement.
     * Implements the behavior of a Path at this node.
     * @param accessContext The context for the search at this node.
     * @param callback The action to take at each accessed node
     */
    public abstract void access(AccessContext accessContext, Consumer<JsonNode> callback);
    /**
     * Returns the textual representation of this PathElement
     * @return The textual representation of this PathElement, as readable by {@link Path#fromPathStr}
     */
    public abstract String toString();
    
    /**
     * @return True when this PathElement represents exactly the same query as other.
     */
    @Override
    public abstract boolean equals(Object other);
    
    /**
     * Compares this PathElement lexically to another.
     * Specifically, the following rules are always obeyed:
     * <ul>
     *   <li>Tags are ordered alphabetically</li>
     *   <li>Indices are ordered numerically</li>
     *   <li>A Path with more branches than, but otherwise identical to, another Path, will come after that other Path</li>
     * <ul>
     * Tags of disparate types have a deterministic, but arbitrary, ordering that may change in future implementations.
     * @param other The PathElement to be compared to
     */
    public int compareTo(PathElement other) {
      if (this.getClass().equals( other.getClass() )) {
        // exact match on class types:
        return this.specCompareTo(other);
      } else {
        // use default class ordering
        return childClasses.indexOf(this.getClass()) - childClasses.indexOf(other.getClass());
      }
    }
    
    /**
     * Passes computeShouldCache calls through to inner Paths
     * @param parentIsConstant Same as for @{link {@link Path#computeShouldCache(Boolean)}
     */
    public void computeShouldCache(Boolean parentIsConstant) {};
    
    /// Protected helpers
    
    /**
     * Implements class-specific comparisons between PathElements of the same class.
     * @param other Another PathElement, guaranteed to be the same class as this.
     * @return An int to be used with compareTo
     */
    protected abstract int specCompareTo(PathElement other);
  }
  
  /**
   * Accesses an attribute.
   * Special tags, like root, reference, and library, can access other parts of a model.
   * 
   * @author David K. Legg
   *
   */
  private static class TagPathElement extends PathElement {
    private static final String WILD   = WILD_TAG;
    private static final String ROOT   = ROOT_TAG;
    private static final String HERE   = HERE_TAG;
    private static final String REF    = REFERENCE_TAG;
    private static final String LIB    = LIBRARY_TAG;
    private static final String PARENT = PARENT_TAG;
    
    private static final List<String> SPECIAL_TAGS = Arrays.asList(HERE, PARENT, ROOT, REF, LIB, WILD);
    
    private String tag;
    
    /// Public methods
    
    public TagPathElement() {
      this.tag = ROOT;
    }
    
    public TagPathElement(String tag) {
      tag = tag.isEmpty() ? HERE : tag;
      
      for (List<String> specialTagList : Arrays.asList(WILD_TAGS, ROOT_TAGS, HERE_TAGS, REFERENCE_TAGS, LIBRARY_TAGS, PARENT_TAGS)) {
        if (specialTagList.contains(tag)) {
          this.tag = specialTagList.get(0);
          return;
        }
      }
      if (tag.startsWith(".")) tag = tag.substring(1); // trim leading dot on normal tags
      this.tag = tag;
    }
    
    public TagPathElement copy() {
      return new TagPathElement(tag);
    }
    
    public void access(AccessContext accessContext, Consumer<JsonNode> callback) {
      switch (tag) {
        case HERE:
          callback.accept(accessContext.object);
          break;
          
        case ROOT:
          callback.accept(accessContext.root);
          break;
          
        case REF:
          callback.accept(accessContext.reference);
          break;

        case PARENT:
          accessContext.object.parent().ifPresent(callback);
          break;
        
        case LIB:
          accessContext.libraries.forEach(callback);
          break;
          
        case WILD:
          accessContext.object.as(ObjectNode.class)
            .map( ObjectNode::values )
            .ifPresent( values -> values.forEach(callback) );
          break;
          
        default:
          accessContext.object.as(ObjectNode.class)
              .flatMap( object -> object.get(tag) )
              .ifPresent(callback);
          break;
      }
    }
    
    @Override
    public int hashCode() {
      if (hashCode == null) {
        hashCode = new HashCodeBuilder(19, 29)
          .append(tag)
          .toHashCode();
      }
      return hashCode;
    }
    
    @Override
    public boolean equals(Object other) {
      return (this == other ||
              (other instanceof TagPathElement &&
               ((TagPathElement) other).tag.equals(this.tag)));
    }
    
    public String toString() {
      return (SPECIAL_TAGS.contains(tag) ?  tag : "." + tag);
    }
    
    /// Protected helpers
  
    protected int specCompareTo(PathElement other) {
      String otherTag = ((TagPathElement) other).tag;
      Boolean thisIsSpecial  = SPECIAL_TAGS.contains(this.tag);
      Boolean otherIsSpecial = SPECIAL_TAGS.contains(otherTag);
      if (thisIsSpecial) {
        if (otherIsSpecial) {
          return SPECIAL_TAGS.indexOf(this.tag) - SPECIAL_TAGS.indexOf(otherTag);
        } else {
          return -1;
        }
      } else if (otherIsSpecial) {
        return 1;
      } else {
        return this.tag.compareTo(otherTag);
      }
    }
  }
  /**
   * Accesses an index of an array.
   * 
   * @author David K. Legg
   *
   */
  private static class IndexPathElement extends PathElement {
    private static final String WILD_STR = WILD_INDEX;
    private static final Integer WILD = -1; // Actually, any illegal index would work. Don't rely on particular value.
    private Integer index;

    /// Public methods
    
    public IndexPathElement() {
      this.index = WILD;
    }
    public IndexPathElement(Integer index) {
      this.index = index;
    }
    
    public IndexPathElement copy() {
      return new IndexPathElement(index);
    }
    
    public void access(AccessContext accessContext, Consumer<JsonNode> callback) {
      if (index.equals(WILD)) {
        accessContext.object.as(ArrayNode.class)
            .map( ArrayNode::values )
            .ifPresent( values -> values.forEach(callback) );
        
      } else {
        accessContext.object.as(ArrayNode.class)
            .flatMap( array -> array.get(index) )
            .ifPresent(callback);
      }
    }
    
    @Override
    public int hashCode() {
      if (hashCode == null) {
        hashCode = new HashCodeBuilder(19, 29)
            .append(index)
            .toHashCode();
      }
      return hashCode;
    }
    
    @Override
    public boolean equals(Object other) {
      return (this == other ||
              (other instanceof IndexPathElement &&
               ((IndexPathElement) other).index.equals(this.index)));
    }
    
    public String toString() {
      return "[" + (index.equals(WILD) ? WILD_STR : index.toString()) + "]";
    }

    /// Protected helpers
    
    protected int specCompareTo(PathElement other) {
      Integer otherIndex = ((IndexPathElement) other).index;
      Boolean thisIsWild = this.index.equals(WILD);
      Boolean otherIsWild = otherIndex.equals(WILD);
      if (thisIsWild) {
        if (otherIsWild) {
          return 0;
        } else {
          return -1;
        }
      } else if (otherIsWild) {
        return 1;
      } else {
        return this.index.compareTo(otherIndex);
      }
    }
  }
  /**
   * Accesses this object and all of its descendents.
   * 
   * @author David K. Legg
   *
   */
  private static class RecursivePathElement extends PathElement {
    
    /// Public methods
    
    public RecursivePathElement() {
    }
    
    public RecursivePathElement copy() {
      return this;
    }
    
    public void access(AccessContext accessContext, Consumer<JsonNode> callback) {
      callback.accept(accessContext.object);
      accessContext.object.children().stream()
        .map( accessContext::swapObject )
        .forEach( newAC -> this.access(newAC, callback) );
    }
    
    @Override
    public int hashCode() {
      if (hashCode == null) hashCode = new HashCodeBuilder(43, 67).toHashCode();
      return hashCode;
    }

    @Override
    public boolean equals(Object other) {
      return (this == other ||
              other instanceof RecursivePathElement);
    }
    
    public String toString() {
      return RECURSIVE_ELEM;
    }
    
    /// Protected helpers
  
    protected int specCompareTo(PathElement other) {
      return 0;
    }
  }
  /**
   * Accesses this object and all of its parents.
   * 
   * @author David K. Legg
   *
   */
  private static class RecursiveUpPathElement extends PathElement {
    
    /// Public methods
    
    public RecursiveUpPathElement() {
    }
    
    public RecursiveUpPathElement copy() {
      return this;
    }
    
    public void access(AccessContext accessContext, Consumer<JsonNode> callback) {
      Optional<JsonNode> current = Optional.of(accessContext.object);
      while (current.isPresent()) {
        callback.accept(current.get());
        current = current.flatMap( JsonNode::parent );
      }
    }
    
    @Override
    public int hashCode() {
      if (hashCode == null) hashCode = new HashCodeBuilder(39, 95).toHashCode();
      return hashCode;
    }
    
    @Override
    public boolean equals(Object other) {
      return (this == other ||
              other instanceof RecursiveUpPathElement);
    }
    
    public String toString() {
      return RECURSIVE_UP_ELEM;
    }
    
    /// Protected helpers
  
    protected int specCompareTo(PathElement other) {
      return 0;
    }
  }
  /**
   * Accesses everything specified by a collection of PathElements.
   * 
   * @author David K. Legg
   *
   */
  private static class AlternationPathElement extends PathElement {
    private List<PathElement> innerElements;

    /// Public methods
  
    public AlternationPathElement(PathElement... innerElements) {
      this.innerElements = new LinkedList<PathElement>();
      for (PathElement innerElement : innerElements) {
        if (innerElement instanceof AlternationPathElement) {
          // absorb an alternation, to prevent multi-level alternations
          this.innerElements.addAll(((AlternationPathElement) innerElement).innerElements);
        } else {
          this.innerElements.add(innerElement);
        }
      }
    }
    
    public AlternationPathElement copy() {
      return new AlternationPathElement( 
          innerElements.stream().map( p -> p.copy() ).toArray( length -> new PathElement[length] ) );
    }

    public void access(AccessContext accessContext, Consumer<JsonNode> callback) {
      innerElements.stream().forEach( e -> e.access(accessContext, callback) );
    }
    
    @Override
    public boolean equals(Object other) {
      return (other instanceof AlternationPathElement) &&
          ( ((AlternationPathElement) other).innerElements.equals(this.innerElements) );
    }
    
    @Override
    public int hashCode() {
      if (hashCode == null) {
        hashCode = new HashCodeBuilder(19, 29)
          .append(innerElements)
          .toHashCode();
      }
      return hashCode;
    }
    
    public String toString() {
      return "[" + innerElements.stream()
          .map( p -> p.toString() )
          .collect( Collectors.joining(",") ) + "]";
    }
  
    /// Protected helpers
  
    protected int specCompareTo(PathElement other) {
      Iterator<PathElement> thisIt  = innerElements.iterator();
      Iterator<PathElement> otherIt = ((AlternationPathElement) other).innerElements.iterator();
      while (thisIt.hasNext() && otherIt.hasNext()) {
        int comp = thisIt.next().compareTo(otherIt.next());
        if (comp != 0) {
          return comp;
        }
      }
      
      if (thisIt.hasNext()) {
        return 1; // this element has more options
      } else if (otherIt.hasNext()) {
        return -1; // this element has fewer options
      } else {
        return 0; // they're exactly the same
      }
    }
  }

  /**
   * Repeats the path query inside as many times as possible, collecting results for every iteration.
   * 
   * @author David K. Legg
   *
   */
  private static class RepeatPathElement extends PathElement {
    private Path innerPath;
    
    /// Public methods
    
    public RepeatPathElement(Path innerPath) {
      this.innerPath = innerPath;
    }
    
    public RepeatPathElement copy() {
      return new RepeatPathElement(innerPath);
    }
    
    public void access(AccessContext accessContext, Consumer<JsonNode> callback) {
      Function<JsonNode, AccessContext> swapRef = node -> {
        AccessContext output = accessContext.swapObject(node);
        output.reference = node;
        return output;
      };
      LinkedHashSet<JsonNode> output = new LinkedHashSet<JsonNode>();
      output.add(accessContext.object);
      Set<JsonNode> frontier = new LinkedHashSet<JsonNode>();
      frontier.add(accessContext.object);
      int oldSize = 0;
      int newSize = 1;
      while (oldSize < newSize) {
        frontier = frontier.stream()
            .flatMap( node -> innerPath.innerAccess(swapRef.apply(node)).stream() ) // re-defines REF to be the last iteration's results while inside a repeat element.
            .collect( Collectors.toSet() );
        output.addAll(frontier);
        oldSize = newSize;
        newSize = output.size();
      }
      output.forEach(callback);
    }
    
    @Override
    public boolean equals(Object other) {
      return (other instanceof RepeatPathElement) &&
          ((RepeatPathElement) other).innerPath.equals(this.innerPath);
    }
    
    @Override
    public int hashCode() {
      if (hashCode == null) {
        hashCode = new HashCodeBuilder(27, 13)
            .append(innerPath)
            .toHashCode();
      }
      return hashCode;
    }
    
    public String toString() {
      return "*(" + innerPath.toString() + ")";
    }
    
    /// Protected helpers
    
    protected int specCompareTo(PathElement other) {
      return this.innerPath.compareTo( ((RepeatPathElement) other).innerPath );
    }
  }

  /**
   * Filter matches based on some conditions.
   * {@link FilterPathElement#access} returns either the current object (match success) or nothing (failure).
   * 
   * @author David K. Legg
   *
   */
  private static abstract class FilterPathElement extends PathElement {
  }
  /**
   * Matches when an internal query fails (returns no values).
   * 
   * @author David K. Legg
   *
   */
  private static class NegationFilterPathElement extends FilterPathElement {
    private Path negatedPath;
    
    /// Public methods
    
    public NegationFilterPathElement(Path negatedPath) {
      this.negatedPath = negatedPath;
    }
    
    public PathElement copy() {
      return new NegationFilterPathElement( negatedPath.copy() );
    }
    
    public void access(AccessContext accessContext, Consumer<JsonNode> callback) {
      AccessResult lookup = negatedPath.innerAccess(accessContext);
      if (lookup.isEmpty()) {
        callback.accept(accessContext.object);
      }
    }
    
    @Override
    public int hashCode() {
      if (hashCode == null) {
        hashCode = new HashCodeBuilder(29, 823)
            .append(negatedPath)
            .toHashCode();
      }
      return hashCode;
    }
    
    @Override
    public boolean equals(Object other) {
      return (this == other ||
              (other instanceof NegationFilterPathElement &&
               ((NegationFilterPathElement) other).negatedPath.equals(this.negatedPath)));
    }
    
    public String toString() {
      return String.format("?(!%s)", negatedPath.toString());
    }
    
    @Override
    public void computeShouldCache(Boolean parentIsConstant) {
      negatedPath.computeShouldCache(parentIsConstant);
    }
    
    /// Protected helpers
  
    protected int specCompareTo(PathElement other) {
      return this.negatedPath.compareTo(((NegationFilterPathElement) other).negatedPath);
    }
  }
  
  /**
   * Matches when an internal query matches a specified constant value.
   * 
   * @author David K. Legg
   *
   */
  private static class AttributeFilterPathElement extends FilterPathElement {
    private static final String WILD = "*";
    
    private Path attribute;
    private String value;
    
    /// Public methods
    
    public AttributeFilterPathElement(Path attribute, String value) {
      this.attribute = attribute;
      this.value     = value;
    }
    
    public PathElement copy() {
      return new AttributeFilterPathElement(attribute.copy(), value);
    }
    
    public void access(AccessContext accessContext, Consumer<JsonNode> callback) {
      attribute.innerAccess(accessContext, node -> {
        if (value.equals(WILD) || 
            node.as(ValueNode.class)
              .map( ValueNode::asText )
              .orElseGet( () -> node.toString() ).equals(value)) {
          callback.accept(accessContext.object);
        }
      });
    }
    
    @Override
    public int hashCode() {
      if (hashCode == null) {
        hashCode = new HashCodeBuilder(75, 23)
            .append(attribute)
            .append(value)
            .toHashCode();
      }
      return hashCode;
    }
    
    @Override
    public boolean equals(Object other) {
      return (this == other ||
              (other instanceof AttributeFilterPathElement &&
               ((AttributeFilterPathElement) other).attribute.equals(this.attribute) &&
               ((AttributeFilterPathElement) other).value.equals(this.value)));
    }

    public String toString() {
      return String.format("?(%s=\"%s\")", attribute.toString(), value);
    }

    @Override
    public void computeShouldCache(Boolean parentIsConstant) {
      attribute.computeShouldCache(parentIsConstant);
    }
    
    /// Protected helpers
  
    protected int specCompareTo(PathElement other) {
      AttributeFilterPathElement otherAttr = (AttributeFilterPathElement) other;
      int attrComp = this.attribute.compareTo(otherAttr.attribute);
      if (attrComp != 0) {
        return attrComp;
      } else {
        return this.value.compareTo(otherAttr.value);
      }
    }
  }

  /**
   * Matches when an internal query matches a specified regular expression.
   * 
   * @author David K. Legg
   *
   */
  private static class RegexFilterPathElement extends FilterPathElement {

	    private Path attribute;
	    private Pattern regex;
	    
	    /// Public methods
	    
	    public RegexFilterPathElement(Path attribute, String value) {
	      this.attribute = attribute;
	      this.regex     = Pattern.compile(value);
	    }
	    
	    public PathElement copy() {
	      return new RegexFilterPathElement(attribute.copy(), regex.toString());
	    }
	    
	    public void access(AccessContext accessContext, Consumer<JsonNode> callback) {
	      attribute.innerAccess(accessContext, node -> {
	        if (regex.matcher(
		        	node.as(ValueNode.class)
		              .map( ValueNode::asText )
		              .orElseGet( () -> node.toString() ))
	        	.matches()) {
	          callback.accept(accessContext.object);
	        }
	      });
	    }
	    
	    @Override
	    public int hashCode() {
	      if (hashCode == null) {
	        hashCode = new HashCodeBuilder(75, 23)
	            .append(attribute)
	            .append(regex)
	            .toHashCode();
	      }
	      return hashCode;
	    }
	    
	    @Override
	    public boolean equals(Object other) {
	      return (this == other ||
	              (other instanceof RegexFilterPathElement &&
	               ((RegexFilterPathElement) other).attribute.equals(this.attribute) &&
	               ((RegexFilterPathElement) other).regex.equals(this.regex)));
	    }

	    public String toString() {
        return String.format("?(%s~=\"%s\")", attribute.toString(), regex.toString());
	    }
	  
	    @Override
	    public void computeShouldCache(Boolean parentIsConstant) {
	      attribute.computeShouldCache(parentIsConstant);
	    }
	    
	    /// Protected helpers

	    protected int specCompareTo(PathElement other) {
	      RegexFilterPathElement otherRegex = (RegexFilterPathElement) other;
	      int attrComp = this.attribute.compareTo(otherRegex.attribute);
	      if (attrComp != 0) {
	        return attrComp;
	      } else {
	        return this.regex.toString().compareTo(otherRegex.regex.toString());
	      }
	    }
	  }
  
  /**
   * Matches when there is overlap between the values returned by two internal queries.
   * 
   * @author David K. Legg
   *
   */
  private static class DoubleAttributeFilterPathElement extends FilterPathElement {
    private Path mainAttribute, comparisonAttribute;

    /// Public methods
    
    public DoubleAttributeFilterPathElement(Path mainAttribute, Path comparisonAttribute) {
      this.mainAttribute       = mainAttribute;
      this.comparisonAttribute = comparisonAttribute;
    }
    
    public PathElement copy() {
      return new DoubleAttributeFilterPathElement(mainAttribute.copy(), comparisonAttribute.copy());
    }
    
    public void access(AccessContext accessContext, Consumer<JsonNode> callback) {
      AccessResult mainAttrLookup = mainAttribute.innerAccess(accessContext),
             comparisonAttrLookup = comparisonAttribute.innerAccess(accessContext);
      
      mainAttrLookup.retainAll(comparisonAttrLookup);
      if ( !mainAttrLookup.isEmpty() ) {
        callback.accept(accessContext.object);
      }
    }
    
    @Override
    public int hashCode() {
      if (hashCode == null) {
        hashCode = new HashCodeBuilder(23, 679)
            .append(mainAttribute)
            .append(comparisonAttribute)
            .toHashCode();
      }
      return hashCode;
    }
    
    @Override
    public boolean equals(Object other) {
      return (this == other ||
              (other instanceof DoubleAttributeFilterPathElement &&
               ((DoubleAttributeFilterPathElement) other).mainAttribute.equals(this.mainAttribute) &&
               ((DoubleAttributeFilterPathElement) other).comparisonAttribute.equals(this.comparisonAttribute)));
    }

    public String toString() {
      return String.format("?(%s=%s)", mainAttribute.toString(), comparisonAttribute.toString());
    }

    @Override
    public void computeShouldCache(Boolean parentIsConstant) {
      mainAttribute.computeShouldCache(parentIsConstant);
      comparisonAttribute.computeShouldCache(parentIsConstant);
    }
    
    /// Protected helpers

    protected int specCompareTo(PathElement other) {
      DoubleAttributeFilterPathElement otherAttr = (DoubleAttributeFilterPathElement) other;
      int mainComp = this.mainAttribute.compareTo(otherAttr.mainAttribute);
      if (mainComp != 0) {
        return mainComp;
      } else {
        return this.comparisonAttribute.compareTo(otherAttr.comparisonAttribute);
      }
    }
  }

  
  /**
   * An extension to the Antlr4 Visitor, to connect parser to Path constructors.
   * 
   * @author David K. Legg
   *
   */
  public static class PathVisitor extends JsonPath2ParserBaseVisitor<Path> {
    @Override
    public Path visitPath(PathContext ctx) {
      ElementVisitor elementVisitor = new ElementVisitor();
      PathElement element;
      if (ctx.recursivepath() != null) {
        element = new RecursivePathElement();
      } else if (ctx.recursiveuppath() != null) {
        element = new RecursiveUpPathElement();
      } else {
        element = ctx.element().accept(elementVisitor);
      }
      Path output = new Path(element);
      if (ctx.branches() != null) {
        ctx.branches().path().stream()
            .map( path -> path.accept(this) )
            .forEach( output::addBranch );
      }
      
      return output;
    }
  }
  private static class ElementVisitor extends JsonPath2ParserBaseVisitor<PathElement> {
    @Override
    public PathElement visitTagelement(TagelementContext ctx) {
      return new TagPathElement(ctx.getText());
    }
    
    @Override
    public PathElement visitIndexelement(IndexelementContext ctx) {
      String indexStr = ctx.index().getText();
      if (indexStr.equals(WILD_INDEX)) {
        return new IndexPathElement();
      } else {
        return new IndexPathElement( Integer.valueOf(indexStr) );
      }
    }
    
    @Override
    public PathElement visitAlternationelement(AlternationelementContext ctx) {
      return new AlternationPathElement(
          ctx.element().stream()
              .map( element -> element.accept(this) ) // build each PathElement
              .toArray( length -> new PathElement[length] )); // package them to give to varargs constructor
    }
    
    @Override
    public PathElement visitRepeatelement(RepeatelementContext ctx) {
      return new RepeatPathElement( ctx.path().accept(new PathVisitor()) );
    }
    
    @Override
    public PathElement visitNegationfilterelement(NegationfilterelementContext ctx) {
      return new NegationFilterPathElement( 
          ctx.path().accept( new PathVisitor() ));
    }
    
    @Override
    public PathElement visitAttributefilterelement(AttributefilterelementContext ctx) {
      return new AttributeFilterPathElement(
          ctx.path().accept( new PathVisitor() ),
          ctx.attributevalue().getText().replaceAll("^\"|\"$", "")); // trim quotes
    }

    @Override
    public PathElement visitRegexfilterelement(RegexfilterelementContext ctx) {
      return new RegexFilterPathElement(
          ctx.path().accept( new PathVisitor() ),
          ctx.attributevalue().getText().replaceAll("^\"|\"$", "")); // trim quotes
    }
    
    @Override
    public PathElement visitDoubleattributefilterelement(DoubleattributefilterelementContext ctx) {
      PathVisitor pathVisitor = new PathVisitor();
      return new DoubleAttributeFilterPathElement(
          ctx.path(0).accept(pathVisitor),
          ctx.path(1).accept(pathVisitor));
    }
  }

  private static class AccessResult extends LinkedList<JsonNode> {
    private static final long serialVersionUID = 8137785917133855389L;
    public AccessResult() {
      super();
    }
    public AccessResult(Collection<? extends JsonNode> c) {
      super(c);
    }
  };
}
