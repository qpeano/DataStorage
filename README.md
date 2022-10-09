# DataStorage - structural data storing using text-and-binary files 

- Structure

A DataStorage is a file consisting of DataUnits, which are containers for data. They have a label which is used for retrieval. The data contained in a DataUnit is called fragments, and come in two forms. A fragment can either be a piece of text or a DataUnit. This allows for nested DataUnits if needed. The DataStorage itself is a text or binary file. If It is a text file it will have a clear structure, See (1). 

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
}
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

- Developement Thus Far

A proof of concept was made in August 2021. Developement was then on-and-off, yealding two more versions (DataCollection, and DataSet), both more or less an extenstion of the proof of concept. These versions, while essential for the developement of my programming ability, were and still are basic and lack the nested DataUnit feature that is present in the DataStorge class. That was the most difficult feature to implement. Months were spent with pen and paper to try to create an algorithm to handle it. The implementation, used as of 2022-10-09, was one of many algorithms, though this one actually made it into code. 

- What Is Next

Extensive testing is still needed to make sure that the nested works for numerous configurations of DataUnits and fragments. The plan after that is to store the DataStorage in a binary file to save space, implementing a method called "snapshot" where user can get the current state of the DataStorage in a text file/ string, with the formatting seen in (1).
