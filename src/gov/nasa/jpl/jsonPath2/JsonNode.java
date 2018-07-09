package gov.nasa.jpl.jsonPath2;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import static gov.nasa.jpl.jsonPath2.Utils.*;

/**
 * Inspired by Jackson's JsonNodes, but immutable and light-weight.
 * 
 * @author dlegg
 *
 */
public abstract class JsonNode implements Comparable<JsonNode> {
  /**
   * Constructs JsonNode tree from a valid JSON string.
   * @param jsonString The textual form of a JSON object.
   * @return The root of a JsonNode tree, using literal string nodes where nothing else will do.
   */
  public static JsonNode readFrom(String jsonString) {
    return buildFrom(firstPresent(
        optOnError( () -> new JSONObject(jsonString) ),
        optOnError( () -> new JSONArray(jsonString) ),
        optOnError( () -> new Integer(jsonString) ),
        optOnError( () -> new Double(jsonString) )
        ).orElse(jsonString));
    
  }
  /**
   * Constructs JsonNode tree from an org.json parse of a JSON string.
   * @param simpleJsonObject Any valid org.json object.
   * @return The corresponding tree in JsonNodes.
   */
  public static <T> JsonNode buildFrom(T simpleJsonObject) {
    return buildFrom(simpleJsonObject, null);
  }
  
  /// Private static members
  
  /**
   * Provides a unique identifier for JsonNodes, that can be cloned for view objects.
   */
  private static long INSTANCE_ID = 0;
  
  /// Private members
  
  private long id;
  private JsonNode parent;
  
  protected Integer hashCode = null;
  
  /// Public methods

  /**
   * @return True if this JsonNode is a leaf, a node with 0 children.
   */
  public boolean isLeaf() {
    return this.children().isEmpty();
  }
  
  /**
   * @return The containing JsonNode for this JsonNode, if there is one.
   */
  public Optional<JsonNode> parent() {
    return Optional.ofNullable(parent);
  }
  
  /**
   * Converts this node to the specified node type if possible.
   * @param nodeType A type of JsonNode, to try to cast this node to.
   * @return An Optional of the casted node, if possible, or an empty Optional if not.
   */
  public <T extends JsonNode> Optional<T> as(Class<T> nodeType) {
    if (nodeType.isInstance(this)) {
      return Optional.of(nodeType.cast(this));
    } else {
      return Optional.empty();
    }
  }
  
  /**
   * @return A JsonNode that is identical in value to this JsonNode, but different in identity (not equals to this).
   */
  public JsonNode deepCopy() {
    return this.deepCopy(null);
  }
  
  /**
   * Returns a stable hashCode based on this node's internal UID.
   */
  @Override
  public int hashCode() {
    if (hashCode == null) {
      hashCode = new HashCodeBuilder(35, 29)
          .append(id)
          .toHashCode();
    }
    return hashCode;
  }
  
  /**
   * Compares objects by stable internal UID.
   */
  @Override
  public boolean equals(Object other) {
    return (other instanceof JsonNode) &&
           (((JsonNode) other).id == this.id);
  }

  /**
   * @return All child JsonNodes of this JsonNode.
   */
  public abstract Collection<JsonNode> children(); 
  
  /**
   * Determines whether other is in the tree with this JsonNode at its root.
   * @param other The JsonNode to look for
   * @return True if this equals other, or if any child of this contains other.
   */
  public boolean contains(JsonNode other) {
    // check for containment quickly by ascending the ancestors of other, looking for this
    Optional<JsonNode> optOther = Optional.ofNullable(other);
    while (optOther.isPresent() && !this.equals(optOther.get())) {
      optOther = optOther.flatMap( JsonNode::parent );
    }
    return optOther.isPresent(); // true when second condition stopped loop, when this was found
  }
  
  /**
   * @return The org.json equivalent object for this JsonNode.
   */
  public abstract Object toJSON();
  
  /**
   * Serializes this JsonNode as a JSON-formatted string.
   */
  public String toString() {
    return this.toJSON().toString();
  }
  
  @Override
  public int compareTo(JsonNode other) {
    return Long.signum(other.id - this.id); // avoids overflow that simple cast might introduce
  }
  
  /// Protected methods
  
  /**
   * Sets the id and parent of a new JsonNode
   * @param parent The container of this JsonNode
   */
  protected JsonNode(JsonNode parent) {
    super();
    this.id = INSTANCE_ID++;
    this.parent = parent;
  }
  
  /**
   * Constructs JsonNode tree from an org.json parse of a JSON string.
   * Will also deep-copy a JsonNode tree.
   * @param simpleJsonObject Any valid org.json object.
   * @param parent The parent of the root of simpleJsonObject.
   * @return The corresponding tree in JsonNodes.
   */
  protected static <T> JsonNode buildFrom(T simpleJsonObject, JsonNode parent) {
    if (simpleJsonObject instanceof JsonNode) {
      return ((JsonNode) simpleJsonObject).deepCopy(parent);
    
    } else if (simpleJsonObject instanceof JSONObject) {
      return new ObjectNode((JSONObject) simpleJsonObject, parent);
      
    } else if (simpleJsonObject instanceof JSONArray) {
      return new ArrayNode((JSONArray) simpleJsonObject, parent);
      
    } else if (simpleJsonObject instanceof Map<?,?>) {
      return new ObjectNode(
          ((Map<?,?>) simpleJsonObject).entrySet().stream()
              .map(onKey( Object::toString ))
              .map(onValue( JsonNode::buildFrom ))
              .collect( simpleToMapCollector() ), parent);
      
    } else if (simpleJsonObject instanceof List<?>) {
      return new ArrayNode(
          ((List<?>) simpleJsonObject).stream()
              .map( JsonNode::buildFrom )
              .collect( Collectors.toList() ), parent);
    } else {
      return new ValueNode(simpleJsonObject, parent);
    }
  }

  /**
   * Constructs a "view node": a potentially differently typed object, which accesses the same data.
   * As such, view nodes are equal to their creators, even though they are of different types.
   * @param viewNode The node from which to make the view.
   * @return That node, with its id and parent updated to this node's id and parent.
   */
  protected <T extends JsonNode> T viewAs(T viewNode) {
    ((JsonNode) viewNode).id = this.id;
    ((JsonNode) viewNode).parent = this.parent;
    return viewNode;
  }

  /**
   * @param newParent The deeply copied parent of this node in the new JsonNode tree.
   * @return A JsonNode that is identical in value to this JsonNode, but different in identity (not equals to this).
   */
  protected abstract JsonNode deepCopy(JsonNode newParent);
}
