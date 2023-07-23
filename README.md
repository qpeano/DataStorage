# DataStorage - structural data storing using text-and-binary files 

- Structure

A DataStorage is a file consisting of DataUnits, which are containers for data. They have a label which is used for retrieval. The data contained in a DataUnit is called contents and consists of fragments, and come in two forms. A fragment can either be a piece of text or a DataUnit. This allows for nested DataUnits if needed. The DataStorage itself is a text or binary file. If It is a text file it will have a clear structure, See (1). 

(1) EX: DataUnits, STATUS__P1 and STATUS_P2, with several inner DataUnits that have different fragments. 

STATUS_P1 {\
&emsp;&emsp;&emsp;&emsp;HP {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;100\
&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;LVL {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;13\
&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;MANNA {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;122\
&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;GOLD {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;98\
&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;EQUIPPED_WEAPON {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;BATTLE_AXE {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;DAMAGE {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;100\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;QUIRK {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;LANDSLIDE\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;}\
}\
STATUS_P2 {\
&emsp;&emsp;&emsp;&emsp;HP {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;89\
&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;LVL {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;18\
&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;MANNA {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;200\
&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;GOLD {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;0\
&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;EQUIPPED_WEAPON {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;LONG_SWORD {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;DAMAGE {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;120\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;QUIRK {\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;FLAME\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;}\
&emsp;&emsp;&emsp;&emsp;}\
}
- Fomatting 

The label of a DataUnit can not contain any special characters. they should only us alphanumeric characters amd underscore. The whitespaces used to distinguish a layer from another is very important, not adding enough whitespace will result in a unit being in a lower layer than it should be. Directly changing the DataStorage file should thus be done very carefully. 
