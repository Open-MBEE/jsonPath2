package gov.nasa.jpl.jsonPath2;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.json.JSONObject;

import static gov.nasa.jpl.jsonPath2.Utils.*;

/**
 * An immutable version of a JSONObject.
 * 
 * @author dlegg
 *
 */
public class ObjectNode extends JsonNode {
  /// Private members
  
  Map<String, JsonNode> map;
  
  /// Public methods
  
  public ObjectNode() {
    this(new JSONObject(), null);
  }
  
  /**
   * Access a particular key in this ObjectNode.
   * @param key The String name of the property of this Object to access.
   * @return The contents of that key, if it is present in this object.
   */
  public Optional<JsonNode> get(String key) {
    return Optional.ofNullable(map.get(key));
  }
  
  /**
   * @return All legal keys for this ObjectNode.
   */
  public Collection<String> keys() {
    return Collections.unmodifiableCollection(map.keySet());
  }
  
  /**
   * @return All the child JsonNodes for this ObjectNode.
   */
  public Collection<JsonNode> values() {
    return Collections.unmodifiableCollection(map.values());
  }
  
  /**
   * @return All pairs of keys and corresponding child nodes.
   */
  public Collection<Entry<String, JsonNode>> entries() {
    return Collections.unmodifiableCollection(map.entrySet());
  }
  
  public Collection<JsonNode> children() {
    return values();
  }

//  @Override
//  public <T extends JsonNode> Optional<T> as(Class<T> nodeType) {
//    if (nodeType.equals(ArrayNode.class)) {
//      return Optional.of(nodeType.cast(this.viewAs(
//            new ArrayNode(
//                this.entries().stream()
//                    .map(onKey(optOnError( k -> Integer.valueOf(k) )))
//                    .filter( entry -> entry.getKey().isPresent() )
//                    .map(onKey( Optional::get ))
//                    .sorted( Comparator.comparing( Entry::getKey ) )
//                    .map( Entry::getValue )
//                    .collect( Collectors.toList() ),
//                null ))));
//    } else {
//      return super.as(nodeType);
//    }
//  }
  
  public JSONObject toJSON() {
    JSONObject output = new JSONObject();
    entries().stream()
      .map(onValue( JsonNode::toJSON ))
      .forEach(consumeEntry( output::put ));
    return output;
  }
  
  /// Protected methods
  
  @Override
  protected JsonNode deepCopy(JsonNode newParent) {
    ObjectNode copy = new ObjectNode();
    copy.map = Collections.unmodifiableMap(entries().stream()
        .map(onValue( child -> child.deepCopy(copy) ))
        .collect( simpleToMapCollector() ));
    return copy;
  }
  
  /// Package-visible constructor
  
  ObjectNode(JSONObject baseObject, JsonNode parent) {
    super(parent);
    map = new LinkedHashMap<>();
    String[] keys = JSONObject.getNames(baseObject);
    if (keys != null) {
      for (String key : keys) {
        map.put(key, JsonNode.buildFrom(baseObject.get(key), this));
      }
    }
    map = Collections.unmodifiableMap(map); // lock it in
  }
  
  ObjectNode(Map<String, JsonNode> baseMap, JsonNode parent) {
    super(parent);
    map = Collections.unmodifiableMap(
        baseMap.entrySet().stream()
            .map(onValue( child -> child.deepCopy(this) ))
            .collect( simpleToMapCollector() ));
  }
}
