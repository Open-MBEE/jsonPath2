# JsonPath2

A query language for JSON documents.

## Installation

Clone this repository, preferably into `~/git/jsonPath2` (other locations may require adjustments). Then, from the root directory for the project, execute

 * `mvn package` - to compile
 * `scripts/pathquery` - to test

If you see a message prompting you for an input path, your install was successful.

The most likely source of errors are dependency failures. Make sure that Antlr4 is installed on your system, and that the Maven dependencies could be fetched.

Also note that when you run `mvn package`, this will install jsonPath2 as a local artifact, which Maven can then use on that machine as a dependency for other projects.

## Description

This language is closely related to [JsonPath](http://goessner.net/articles/JsonPath/), but is **not** compatible with it. For a complete description of the grammar, see the Antlr [Grammar]. Briefly, a Path is a tree structure, with nodes called *elements* and branches called, well, *branches*. Syntactically, a path has one of three forms:

 * Leaf: `.element`
 * linear: `.element.with.one.branch`
 * branching: `.element[.with.branch.one,.and.two,.and.three]`

where an element is any of the following:

 * Tags
     * **root** tag: `$`, `DOC`, or `DOC:`
     * **here** tag: `~`, `HERE`, or `HERE:`
     * **reference** tag: `^`, `REF`, or `REF:`
     * **library** tag: `LIB` or `LIB:`
     * **wild** tag: `.*`
     * **recursive descent** tag: `.**`
     * **parent** tag: `.^`
     * **recursive ascent** tag: `.^^`
     * general tag: `.tagName`
 * Indexes
     * **wild** index: `[*]`
     * general index: `[0]`
 * Element alternations: `[.tag,[0],$]`
 * Filter elements
     * Negation filter: `?(!.path.to.negate)`
     * Attribute filter: `?(.path.to.attribute = "value")`
     * Regex filter: `?(.path.to.attribute ~= "regex value")`
     * Double Attribute filter: `?(.path.to.main = .path.to.other)`
 * Repetitions: `*( .path.to.repeat )`

Semantically, every path operates by matching its element to the current object. If that match succeeds, it returns the accessed object, which is given to all of that path's branches to access. The result of the branches' access is collected and returned. If a path is a leaf, it returns what its element matches. The elements match according to the following rules:

 * Tags
     * Root: returns the root of the model on which the query was run, regardless of current object.
     * Here: returns the current object
     * Reference: Inside of a repetition, returns the result of the last iteration of repetition. Outside of a repetition, behavior is context-dependent.
     * Library: returns all library objects, regardless of current object.
     * Wild: returns the value of every attribute of the current object. Fails if the current object is an array, or a terminal (primitive) value.
     * Recursive Descent: returns the current object and all its descendents (children, grandchildren, etc.). In other words, the entire sub-tree rooted at the current object.
     * Parent: returns the object that contains the current object.
     * Recursive ascent: returns the current object, and all its ancestors (parent, grandparent, etc.). In other words, the entire path from the document root to this node.
     * General: returns the value of the specified attribute. Fails if the current object does not have the specified attribute.
 * Indexes
     * Wild: returns the value of every index. Fails if the current object is not an array.
     * General: returns the value of specified index, in 0-based indexing. Fails if the current object is not an array, or is shorter than given index.
 * Alternations: returns the union of all values returned by its inner elements.
 * Filter elements: return the current value, if they succeed.
     * Negation: Succeeds when inner path fails
     * Attribute: Succeeds when inner path returns a value equal to given value (by String comparison)
     * Regex: Succeeds when inner path returns a value that matches the regex when converted to a string
     * Double Attribute: Succeeds when inner paths return 1 or more of the same values.
 * Repetitions: repeatedly applies the path inside to the result set, returning all elements from any number of repetitions.

For example, the following path is used to look up the id of all "Property" type elements in a model (notice the spaces, which are valid):
```
DOC.elements[*]?(.type = "Property").id
```

For a more complex example, the following path is used to look up the names of every element that is "equal" to `REF`. Notice the use of `REF` inside the repetition, where it refers to the results of the previous iteration.
```
DOC.elements[*]
    ?(.id =
        REF *(
            DOC.elements[*]
                ?(.type = "Equals")
                [
                    ?(.source = REF).target,
                    ?(.target = REF).source
                ]) )
    .name
```

## Command-Line Usage

To invoke JsonPath2 from the command line, run the [pathquery](../scripts/pathquery) script, `scripts/pathquery`. It can be run in either one-off or interactive mode. To run in one-off mode, run:
```
pathquery -path .your.path -file path/to/file.json
```
This will execute `.your.path` against the JSON model in `path/to/file.json`, and report the results. More commonly, however, this script is run in interactive mode. To do so, simply omit the `-path` option, like this:
```
pathquery -file path/to/file.json
```
This will read in the JSON model in `path/to/file.json` and prompt you for a JsonPath2 query. Type in your query, and finish it with a blank line (queries are allowed to span consecutive lines). The script will execute this query, and if it was successful, will report the results, then choose one of the results to be the `REF` object for the next query. To terminate the interactive session, just enter a blank line instead of a path.

Finally, it's often helpful to run this script in verbose mode, by passing the `-v` flag. For more information, and to see all of the accepted flags, open the help by passing the `-h` flag.

[Grammar]: ../grammars/JsonPath2Parser.g4