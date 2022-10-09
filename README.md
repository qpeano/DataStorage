# DataStorage - structural data storing using text-and-binary files 

- Structure

A DataStorage is a file consisting of DataUnits, which are containers for data. They have a label which is used for retrieval. The data contained in a DataUnit is called fragments, and come in two forms. A fragment can either be a piece of text or a DataUnit. This allows for nested DataUnits if needed. The DataStorage itself is a text or binary file. If It is a text file it will have a clear structure, See (1). 

(1) EX: DataUnits, STATUS__P1 and STATUS_P2, with several inner DataUnits that have different fragments. 

STATUS_P1 {\
    HP {
        100
    }
    LVL {
        13
    }
    MANNA {
        122
    }
    GOLD {
        98
    }
    EQUIPPED_WEAPON {
        BATTLE_AXE {
            DAMAGE {
                100
            }
            QUIRK {
                LANDSLIDE
            }
        }
    }
}

STATUS_P2 {
    HP {
        89
    }
    LVL {
        12
    }
    MANNA {
        990
    }
    GOLD {
        0
    }
    EQUIPPED_WEAPON {
        LONG_SWORD {
            DAMAGE {
                122
            }
            QUIRK {
                FLAME
            }
        }
    }
}

- Developement Thus Far

A proof of concept was made in August 2021. Developement was then on-and-off, yealding two more versions (DataCollection, and DataSet), both more or less an extenstion of the proof of concept. These versions, while essential for the developement of my programming ability, were and still are basic and lack the nested DataUnit feature that is present in the DataStorge class. That was the most difficult feature to implement. Months were spent with pen and paper to try to create an algorithm to handle it. The implementation, used as of 2022-10-09, was one of many algorithms, though this one actually made it into code. 

- What Is Next

Extensive testing is still needed to make sure that the nested works for numerous configurations of DataUnits and fragments. The plan after that is to store the DataStorage in a binary file to save space, implementing a method called "snapshot" where user can get the current state of the DataStorage in a text file/ string, with the formatting seen in (1).
