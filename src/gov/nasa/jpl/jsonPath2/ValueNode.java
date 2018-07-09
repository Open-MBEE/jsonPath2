package gov.nasa.jpl.jsonPath2;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.JSONObject;

public class ValueNode extends JsonNode {
  /// Private members
  private Object value;
  
  /// Public methods
  
  /**
   * @return The value contained by this node.
   */
  public Object value() {
    return value;
  }
  
  /**
   * @return value, converted to a String
   */
  public String asText() {
    return value.toString();
  }
  
  public Object toJSON() {
    return JSONObject.wrap(value);
  }
  
  /**
   * Returns the empty collection, because ValueNodes are leaves on a JsonNode tree.
   */
  public Collection<JsonNode> children() {
    return Collections.emptyList();
  }
  
  @Override
  public int hashCode() {
    if (hashCode == null) {
      hashCode = new HashCodeBuilder(43, 57)
        .append(value)
        .toHashCode();
    }
    return hashCode;
  }
  
  @Override
  public boolean equals(Object other) {
    return (other instanceof ValueNode) &&
        ((ValueNode) other).value.equals(this.value);
  }
  
  /// Protected methods
  @Override
  protected JsonNode deepCopy(JsonNode newParent) {
    return new ValueNode(value, newParent);
  }
  
  /// Package-visible constructors
  ValueNode(Object value, JsonNode parent) {
    super(parent);
    this.value = value;
  }
}
