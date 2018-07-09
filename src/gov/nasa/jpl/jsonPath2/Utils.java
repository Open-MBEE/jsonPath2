package gov.nasa.jpl.jsonPath2;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A collection of useful functions, that fall into two categories:
 * <ul>
 *   <li>K specific functions, which are generic to all classes in this package.</li>
 *   <li>Totally generic utility functions.</li>
 * <ul>
 * 
 * @author David K. Legg
 *
 */
public class Utils {

  /// Public methods
  
  /**
   * Reads an entire stream as a String.
   * Particularly useful for file streams.
   * @param stream The InputStream to read.
   * @return The String given when InputStream is completely read.
   */
  public static String streamToString(InputStream stream) {
    try (Scanner s = new Scanner(stream)) {
      String output = s.useDelimiter("\\Z").next();
      return output;
    }
  }

  // Stream.map utils:
  /**
   * "Splits" a stream, converting it to a Stream of entries.
   * @return A stream of entry, each containing a duplication of the original stream elements.
   */
  public static <T> Function< T, Map.Entry<T,T> > splitStream() {
    return x -> new AbstractMap.SimpleEntry<>(x,x);
  }
  /**
   * "Splits" a stream by a mapping function, like keeping a copy in reserve.
   * @param f The mapping function to operate on stream
   * @return Stream of entries of the form (f(x), x) for each x in the original stream. 
   */
  public static <T,U> Function< T, Map.Entry<U,T> > splitStream( Function<T,U> f ) {
    return x -> new AbstractMap.SimpleEntry<>(f.apply(x), x);
  }
  /**
   * Generates a consumer for entry keys
   * @param f A consumer for the key of those entries.
   * @return A consumer for the entries, that applies f to the key of the entry.
   */
  public static <T> Consumer<Map.Entry<T, ?>> consumeKey( Consumer<T> f ) {
    return entry -> f.accept(entry.getKey());
  }
  /**
   * Generates a consumer for entry values
   * @param f A consumer for the value of those entries.
   * @return A consumer for the entries, that applies f to the value of the entry.
   */
  public static <T> Consumer<Map.Entry<?, T>> consumeValue( Consumer<T> f ) {
    return entry -> f.accept(entry.getValue());
  }
  /**
   * Generates a consumer for entries
   * @param f A biconsumer for the key and value of those entries.
   * @return A consumer for the entries, that applies f to the entry.
   */
  public static <K,V> Consumer<Map.Entry<K,V>> consumeEntry( BiConsumer<K,V> f ) {
    return entry -> f.accept(entry.getKey(), entry.getValue());
  }
  /**
   * Generates a function for changing entry keys
   * @param f A function that operates on entry keys
   * @return A function that takes entries (k, v) -> (f(k), v)
   */
  public static <T,U,V> Function< Map.Entry<T,V>, Map.Entry<U,V> > onKey( Function<T,U> f ) {
    return entry -> new AbstractMap.SimpleEntry<>( f.apply(entry.getKey()), entry.getValue() );
  }
  /**
   * Generates a function for changing entry keys
   * @param f A function that operates on entry keys
   * @return A function that takes entries (k, v) -> (k, f(v))
   */
  public static <T,U,V> Function< Map.Entry<T,U>, Map.Entry<T,V> > onValue( Function<U,V> f ) {
    return entry -> new AbstractMap.SimpleEntry<>( entry.getKey(), f.apply(entry.getValue()) );
  }
  /**
   * Generates a function for entries from a BiFunction
   * @param f A function that takes entry keys and values
   * @return A function that takes entries (k, v) -> f(k, v)
   */
  public static <K,V,T> Function< Map.Entry<K, V>, T > onEntry( BiFunction<K,V,T> f ) {
    return entry -> f.apply(entry.getKey(), entry.getValue());
  }
  
  /**
   * A collector for turning a Stream of Entries into a Map.
   * @return toMap collector that takes Entries, and leaves them untouched to put into the map
   */
  public static <K,V> Collector<Map.Entry<K,V>,?,Map<K,V>> simpleToMapCollector() {
    return Collectors.toMap(Map.Entry<K,V>::getKey, Map.Entry<K,V>::getValue);
  }

  /**
   * Converts an optional to a corresponding stream.
   * @param opt The optional that might contain the desired data.
   * @return A stream of that data, if present, or an empty stream if not.
   */
  public static <T> Stream<T> optStream(Optional<T> opt) {
    return opt.map( Stream::of ).orElseGet( Stream::empty );
  }
  /**
   * Returns a function to convert optionals to streams.
   * Especially useful as <code>stream.flatMap(optStream())</code>,
   * which does an equivalent job to <code>stream.filter(Optional::isPresent).map(Optional::get)</code>
   * @return {@link Utils#optStream(Optional)}
   */
  public static <T> Function<Optional<T>, Stream<T>> optStream() {
    return Utils::optStream;
  }
  
  
  // Optional utils
  @FunctionalInterface
  public interface Action {
    void run();
  }
  @FunctionalInterface
  public interface ThrowingAction {
    void run() throws Throwable;
  }
  @FunctionalInterface
  public interface ThrowingSupplier<T> {
    T get() throws Throwable;
  }
  @FunctionalInterface
  public interface ThrowingFunction<T,U> {
    U apply(T t) throws Throwable;
  }
  @FunctionalInterface
  public interface ThrowingBiFunction<T,U,V> {
    V apply(T t, U u) throws Throwable;
  }
  /**
   * Converts a Supplier from exception-throwing to Optional paradigm.
   * @param f A Supplier that could throw some Throwable (exception)
   * @return A new function that returns Optional.of(result) if f returns result, or empty Optional if exception was thrown.
   */
  public static <T> Supplier<Optional<T>> optOnError( ThrowingSupplier<T> f ) {
    return () -> {
      try {
        return Optional.of(f.get());
      } catch (Throwable e) {
        return Optional.empty();
      }
    };
  }
  /**
   * Converts a Function from exception-throwing to Optional paradigm.
   * @param f A BiFunction that could throw some Throwable (exception)
   * @return A new BiFunction that returns Optional.of(result) if f returns result, or empty Optional if exception was thrown.
   */
  public static <T,U> Function<T,Optional<U>> optOnError( ThrowingFunction<T,U> f ) {
    return t -> {
      try {
        return Optional.of(f.apply(t));
      } catch (Throwable e) {
        return Optional.empty();
      }
    };
  }
  /**
   * Converts a BiFunction from exception-throwing to Optional paradigm.
   * @param f A BiFunction that could throw some Throwable (exception)
   * @return A new BiFunction that returns Optional.of(result) if f returns result, or empty Optional if exception was thrown.
   */
  public static <T,U,V> BiFunction<T,U,Optional<V>> optOnError( ThrowingBiFunction<T,U,V> f ) {
    return (t,u) -> {
      try {
        return Optional.of(f.apply(t,u));
      } catch (Throwable e) {
        return Optional.empty();
      }
    };
  }
  
  @SafeVarargs
  /**
   * Executes a series of optional-returning functions.
   * @param inputs A sequence of potentially successful operations.
   * @return The first non-empty optional, if it exists.
   */
  public static <T> Optional<T> firstPresent(Supplier<Optional<T>>... inputs) {
    Optional<T> output = Optional.empty();
    for (Supplier<Optional<T>> supplier : inputs) {
      output = supplier.get();
      if (output.isPresent()) break;
    }
    return output;
  }
  
  // Console utils
  public static Function<String[], Map<String, String>> optionReader(Collection<List<String>> longOptions, Collection<List<String>> flagOptions) {
    // builds a map from the canonical form of the option to all synonyms, as lower-case
    Function<Collection<List<String>>, Map<String,Set<String>>> toLowerSets = options -> options.stream()
        .map( splitStream( list -> list.get(0) ) )
        .map(onValue( synonyms -> synonyms.stream()
            .map( String::toLowerCase )
            .collect( Collectors.toSet() ) ))
        .collect( simpleToMapCollector() );
    
    Map<String, Set<String>> lowerLongOptions = toLowerSets.apply(longOptions);
    Map<String, Set<String>> lowerFlagOptions = toLowerSets.apply(flagOptions);
    
    Function<String, Optional<String>> findLong = str -> lowerLongOptions.entrySet().stream()
        .filter( entry -> entry.getValue().contains(str.toLowerCase()) )
        .map( Entry::getKey )
        .findAny();
    
    Function<String, List<String>> trueFlags  = flag -> Arrays.asList(flag, flag + "on");
    Function<String, List<String>> falseFlags = flag -> Arrays.asList(flag, "no" + flag, "not" + flag, flag + "off");
    
    Function<Function<String, List<String>>, Function<String, Optional<String>>> findFlag = transform -> str -> lowerFlagOptions.entrySet().stream()
        .filter( entry -> entry.getValue().stream()
            .flatMap( synonym -> transform.apply(synonym).stream() )
            .anyMatch( synonym -> str.equalsIgnoreCase(synonym) ))
        .map( Entry::getKey )
        .findAny();
    
    Function<String, Optional<String>> findTrueFlag  = findFlag.apply(trueFlags);
    Function<String, Optional<String>> findFalseFlag = findFlag.apply(falseFlags);
    
    Function<String, String> trimDashes = str -> {
      int i = 0;
      while (str.charAt(i) == '-') ++i;
      return str.substring(i);
    };
    
    return args -> {
      Map<String, String> output = new LinkedHashMap<>();
      Iterator<String> argIter = Arrays.asList(args).iterator();
      
      while (argIter.hasNext()) {
        String arg = trimDashes.apply( argIter.next().toLowerCase() );
        Optional<String> longOpt = findLong.apply(arg);
        Optional<String> trueFlagOpt  = findTrueFlag.apply(arg);
        Optional<String> falseFlagOpt = findFalseFlag.apply(arg);
        if (longOpt.isPresent()) {
          // add this key, and consume the next argument if it exists.
          output.put( longOpt.get(), optOnError( () -> argIter.next() ).get().orElse("") );
        } else if (trueFlagOpt.isPresent()) {
          output.put( trueFlagOpt.get(), "true" );
        } else if (falseFlagOpt.isPresent()) {
          output.put( falseFlagOpt.get(), "false" );
        } else {
          output.put( arg, null );
        }
      }
      
      return output;
    };
  }
  
  // Logging utils
  public static class Logger {
    public enum VERBOSITY { SILENT, QUIET, DEFAULT, VERBOSE, ALL };
    public enum MSG_TYPE  { DEFAULT, ERROR, WARNING, DEBUG, OUTPUT, OTHER };

    public static final Logger SILENT_LOGGER = makeSilentLogger();
    private static Logger makeSilentLogger() {
      Logger logger = new Logger();
      logger.setMaxVerbosity(VERBOSITY.SILENT);
      return logger;
    }
    
    private Map<MSG_TYPE, VERBOSITY> verbosityLevels = new LinkedHashMap<>();
    private Map<MSG_TYPE, OutputStream> logStreams = new LinkedHashMap<>();
    
    public Logger() {
      for (MSG_TYPE type : MSG_TYPE.values()) {
        if (type.equals(MSG_TYPE.ERROR) || type.equals(MSG_TYPE.WARNING)) {
          setOutputStream( System.err, type );
        } else {
          setOutputStream( System.out, type );
        }
      }
      setVerbosity( VERBOSITY.DEFAULT );
    }
    
    // Basic logging functions:
    
    public void log(String msg, Object... args) {
      log(msg, VERBOSITY.DEFAULT, args);
    }
    public void log(String msg, VERBOSITY level, Object... args) {
      log(msg, level, MSG_TYPE.DEFAULT, args);
    }
    public void log(String msg, MSG_TYPE type, Object... args) {
      log(msg, VERBOSITY.DEFAULT, type, args);
    }
    public void log(String msg, VERBOSITY level, MSG_TYPE type, Object... args) {
      try {
        msg = String.format(msg, args) + "\n"; // add a newline for convenience
        
        // Get the controlling verbosity level:
        VERBOSITY verbControl = verbosityLevels.get(type); // specific to this message, or...
        if (verbControl == null) verbControl = verbosityLevels.get(MSG_TYPE.DEFAULT); // the globally set default, or...
        if (verbControl == null) verbControl = VERBOSITY.DEFAULT; // the default when you haven't even set the default
        
        // if controlling level is lower, break out.
        if (verbControl.compareTo(level) < 0) return;
        
        // Get the correct output stream:
        OutputStream outputStream = logStreams.get(type); // specific to this message type, or...
        if (outputStream == null) outputStream = logStreams.get(MSG_TYPE.DEFAULT);
        if (outputStream == null) {
          // This is an erroneous state for the logger, so record that:
          outputStream = logStreams.getOrDefault(MSG_TYPE.ERROR, System.err);
          if (outputStream != null) {
              outputStream.write("LOGGER ERROR: Message could not be written to an appropriate output stream.".getBytes());
          } // else, all outputs we could reasonably use are being suppressed, so silently ignore the error.
        }
        // regardless of whether the outputStream is in an appropriate one, if we can write any message, we will.
        if (outputStream != null) outputStream.write(msg.getBytes());
        
      } catch (Throwable e) {
        // We will *never* throw an error from this method
        e.printStackTrace();
      }
    }
  
    // Convenience forms:
    
    public void verbose(String msg, Object... args) {
      log(msg, VERBOSITY.VERBOSE, args);
    }
    
    public void error(String msg, Object... args) {
      error(msg, VERBOSITY.DEFAULT, args);
    }
    public void error(String msg, VERBOSITY level, Object... args) {
      log("ERROR: " + msg, level, MSG_TYPE.ERROR, args);
    }
    
    public void warning(String msg, Object... args) {
      warning(msg, VERBOSITY.DEFAULT, args);
    }
    public void warning(String msg, VERBOSITY level, Object... args) {
      log("WARNING: " + msg, level, MSG_TYPE.WARNING, args);
    }
    
    public void debug(String msg, Object... args) {
      debug(msg, VERBOSITY.DEFAULT, args);
    }
    public void debug(String msg, VERBOSITY level, Object... args) {
      log("DEBUG: " + msg, level, MSG_TYPE.DEBUG, args);
    }
    
    public void output(String msg, Object... args) {
      output(msg, VERBOSITY.DEFAULT, args);
    }
    public void output(String msg, VERBOSITY level, Object... args) {
      log(msg, level, MSG_TYPE.OUTPUT, args);
    }

    public void other(String msg, Object... args) {
      other(msg, VERBOSITY.DEFAULT, args);
    }
    public void other(String msg, VERBOSITY level, Object... args) {
      log(msg, level, MSG_TYPE.OTHER, args);
    }

    public <T> Function<Optional<T>, Optional<T>> logIfEmpty(String msg, Object... args) {
      return logIfEmpty(msg, VERBOSITY.DEFAULT, args);
    }
    public <T> Function<Optional<T>, Optional<T>> logIfEmpty(String msg, VERBOSITY level, Object... args) {
      return logIfEmpty(msg, level, MSG_TYPE.DEFAULT, args);
    }
    public <T> Function<Optional<T>, Optional<T>> logIfEmpty(String msg, MSG_TYPE type, Object... args) {
      return logIfEmpty(msg, VERBOSITY.DEFAULT, type, args);
    }
    public <T> Function<Optional<T>, Optional<T>> logIfEmpty(String msg, VERBOSITY level, MSG_TYPE type, Object... args) {
      return opt -> {
        if (!opt.isPresent()) log(msg, level, type, args);
        return opt;
      };
    }

    public <T> Function<Optional<T>, Optional<T>> errorIfEmpty(String msg, Object... args) {
      return errorIfEmpty(msg, VERBOSITY.DEFAULT, args);
    }
    public <T> Function<Optional<T>, Optional<T>> errorIfEmpty(String msg, VERBOSITY level, Object... args) {
      return logIfEmpty(msg, level, MSG_TYPE.ERROR, args);
    }

    public <T> Function<Optional<T>, Optional<T>> warningIfEmpty(String msg, Object... args) {
      return warningIfEmpty(msg, VERBOSITY.DEFAULT, args);
    }
    public <T> Function<Optional<T>, Optional<T>> warningIfEmpty(String msg, VERBOSITY level, Object... args) {
      return logIfEmpty(msg, level, MSG_TYPE.WARNING, args);
    }

    public <T> Function<Optional<T>, Optional<T>> debugIfEmpty(String msg, Object... args) {
      return debugIfEmpty(msg, VERBOSITY.DEFAULT, args);
    }
    public <T> Function<Optional<T>, Optional<T>> debugIfEmpty(String msg, VERBOSITY level, Object... args) {
      return logIfEmpty(msg, level, MSG_TYPE.DEBUG, args);
    }
    
    public <T> Function<Optional<T>, Optional<T>> outputIfEmpty(String msg, Object... args) {
      return outputIfEmpty(msg, VERBOSITY.DEFAULT, args);
    }
    public <T> Function<Optional<T>, Optional<T>> outputIfEmpty(String msg, VERBOSITY level, Object... args) {
      return logIfEmpty(msg, level, MSG_TYPE.OUTPUT, args);
    }

    public <T> Function<Optional<T>, Optional<T>> otherIfEmpty(String msg, Object... args) {
      return otherIfEmpty(msg, VERBOSITY.DEFAULT, args);
    }
    public <T> Function<Optional<T>, Optional<T>> otherIfEmpty(String msg, VERBOSITY level, Object... args) {
      return logIfEmpty(msg, level, MSG_TYPE.OTHER, args);
    }
    
    // Setters:
    
    /**
     * Sets all message types to the specified verbosity level, except OUTPUT.
     * @param level The verbosity level to use for all message types.
     */
    public void setVerbosity(VERBOSITY level) {
      for (MSG_TYPE type : MSG_TYPE.values()) {
        if (type.equals(MSG_TYPE.OUTPUT)) continue;
        setVerbosity(level, type);
      }
    }
    public void setVerbosity(VERBOSITY level, MSG_TYPE type) {
      try {
        verbosityLevels.put(type, level);
      } catch (Throwable e) {
        log("LOGGER ERROR:", VERBOSITY.QUIET, MSG_TYPE.ERROR);
        log(e.getMessage(), VERBOSITY.QUIET, MSG_TYPE.ERROR);
      }
    }
    
    /**
     * Caps all verbosities at the given level, except OUTPUT.
     * If a message type has a lower verbosity than level, it will retain that lower verbosity.
     * @param level The maximum verbosity level for every message type.
     */
    public void setMaxVerbosity(VERBOSITY level) {
      for (MSG_TYPE type : MSG_TYPE.values()) {
        if (type.equals(MSG_TYPE.OUTPUT)) continue;
        setMaxVerbosity(level, type);
      }
    }
    /**
     * Caps the given message type's verbosity at the given level.
     * If that message type has a lower verbosity than level, it will retain that lower verbosity.
     * @param level The maximum verbosity level for type
     * @param type The message type to cap verbosity on
     */
    public void setMaxVerbosity(VERBOSITY level, MSG_TYPE type) {
      if (!verbosityLevels.containsKey(type) ||
          verbosityLevels.get(type).compareTo(level) > 0) {
        setVerbosity(level, type);
      }
    }

    /**
     * Lifts all verbosities to at least the given level, except OUTPUT.
     * If a message type has a higher verbosity than level, it will retain that higher verbosity.
     * @param level The minimum verbosity level for every message type.
     */
    public void setMinVerbosity(VERBOSITY level) {
      for (MSG_TYPE type : MSG_TYPE.values()) {
        if (type.equals(MSG_TYPE.OUTPUT)) continue;
        setMinVerbosity(level, type);
      }
    }
    /**
     * Lifts the given message type's verbosity to at least the given level.
     * If that message type has a higher verbosity than level, it will retain that higher verbosity.
     * @param level The minimum verbosity level for type
     * @param type The message type to lift verbosity on
     */
    public void setMinVerbosity(VERBOSITY level, MSG_TYPE type) {
      if (!verbosityLevels.containsKey(type) ||
          verbosityLevels.get(type).compareTo(level) < 0) {
        setVerbosity(level, type);
      }
    }
    
    /**
     * Sets a outputStream for all message types, including OUTPUT.
     * @param outputStream The stream to write all message types to.
     */
    public void setOutputStream(OutputStream outputStream) {
      for (MSG_TYPE type : MSG_TYPE.values()) {
        setOutputStream(outputStream, type);
      }
    }
    /**
     * Sets the outputStream for given message type.
     * @param outputStream The stream to write messages of type to.
     * @param type The particular kind of messages that should go to that stream.
     */
    public void setOutputStream(OutputStream outputStream, MSG_TYPE type) {
      try {
        logStreams.put(type, outputStream);
      } catch (Throwable e) {
        log("LOGGER ERROR:", MSG_TYPE.ERROR);
        log(e.getMessage(), MSG_TYPE.ERROR);
      }
    }
  }
  
  /// Protected methods
  
  /**
   * Reads a resource, like those included in the resources folder, out as a String.
   * @param resourceName the fully qualified file name, with respect to the included resources
   * @return A String containing the contents of the file
   */
  protected static String readResource(String resourceName) {
    return streamToString( Utils.class.getResourceAsStream(resourceName) );
  }
  
  /**
   * Prints and returns its argument.
   * Useful for inspecting calculations in place.
   * @param thing The thing to be printed
   * @return thing, unchanged
   */
  protected static <T> T peekPrint(T thing) {
    System.out.printf("PEEK: %s%n", thing);
    return thing;
  }
}

