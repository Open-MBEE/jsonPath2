package gov.nasa.jpl.jsonPath2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;

import static gov.nasa.jpl.jsonPath2.Utils.*;

/**
 * An Array of JsonNodes in a JsonTree.
 * 
 * @author dlegg
 *
 */
public class ArrayNode extends JsonNode {
  /// Private members
  
  private List<JsonNode> values;
  
  /// Public methods
  
  public ArrayNode() {
    super(null);
  }
  
  /**
   * Access a particular index of the array.
   * @param index The 0-based position in the array to access.
   * @return The contents of that position, if that position is a legal index.
   */
  public Optional<JsonNode> get(int index) {
    return optOnError( () -> values.get(index) ).get();
  }
  
  /**
   * @return All the JsonNodes in this array.
   */
  public Collection<JsonNode> values() {
    return Collections.unmodifiableCollection(values);
  }
  
  /**
   * @return All the JsonNodes in this array, paired with their indices.
   */
  public Collection<Entry<Integer, JsonNode>> entries() {
    return Collections.unmodifiableCollection(
        IntStream.range(0, values.size())
            .boxed()
            .map( splitStream() )
            .map(onValue( values::get ))
            .collect( Collectors.toList() ));
  }
  
  public Collection<JsonNode> children() {
    return values();
  }
  
//  @Override
//  public <T extends JsonNode> Optional<T> as(Class<T> nodeType) {
//    if (nodeType.equals(ObjectNode.class)) {
//      return Optional.of(nodeType.cast(this.viewAs(
//            new ObjectNode(
//                this.entries().stream()
//                    .map(onKey( i -> i.toString() ))
//                    .collect( simpleToMapCollector() ),
//                null ))));
//    } else {
//      return super.as(nodeType);
//    }
//  }
  
  public JSONArray toJSON() {
    return new JSONArray(values.stream().map( JsonNode::toJSON ).collect( Collectors.toList() ));
  }
  
  /// Protected methods
  @Override
  protected JsonNode deepCopy(JsonNode newParent) {
    ArrayNode copy = new ArrayNode();
    copy.values = Collections.unmodifiableList(
        values.stream().map( child -> child.deepCopy(copy) ).collect( Collectors.toList() ) );
    return copy;
  }
  
  /// Package-visible constructors
  ArrayNode(JSONArray baseArray, JsonNode parent) {
    super(parent);
    values = new ArrayList<>(baseArray.length());
    for (int i = 0; i < baseArray.length(); ++i) {
      values.add(JsonNode.buildFrom(baseArray.get(i), this));
    }
    values = Collections.unmodifiableList(values);
  }

  ArrayNode(List<JsonNode> values, JsonNode parent) {
    super(parent);
    this.values = Collections.unmodifiableList(
        values.stream()
          .map( child -> child.deepCopy(this) )
          .collect( Collectors.toList() ));
  }
}
