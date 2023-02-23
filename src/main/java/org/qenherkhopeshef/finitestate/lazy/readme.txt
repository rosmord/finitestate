In a first version, the MultiState accept() method sent back another MetaState. 
This can be correct, but the problem is then that those states can't be easily mixed (we could have ended with two metastate with
the same information partly shared (find concrete examples, using the kleene star for instance - maybe something like 
((ab)*(ab)*)).

Returning a set of states avoids the need to provide something like a "merge" operation.




  