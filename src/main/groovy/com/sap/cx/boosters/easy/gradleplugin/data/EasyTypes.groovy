package com.sap.cx.boosters.easy.gradleplugin.data

class EasyTypes {
    Set<CollectionType> collectiontypes = new HashSet<>()
    Set<EnumerationType> enumtypes = new HashSet<>()
    Set<MapType> maptypes = new HashSet<>()
    Set<RelationType> relationtypes = new HashSet<>()
    Set<ItemType> itemtypes = new HashSet<>()

    EasyTypes(){
        super()
    }

    Set<CollectionType> getCollectiontypes() {
        return collectiontypes
    }

    void setCollectiontypes(Set<CollectionType> collectiontypes) {
        this.collectiontypes = collectiontypes
    }

    Set<EnumerationType> getEnumtypes() {
        return enumtypes
    }

    void setEnumtypes(Set<EnumerationType> enumtypes) {
        this.enumtypes = enumtypes
    }

    Set<MapType> getMaptypes() {
        return maptypes
    }

    void setMaptypes(Set<MapType> maptypes) {
        this.maptypes = maptypes
    }

    Set<RelationType> getRelationtypes() {
        return relationtypes
    }

    void setRelationtypes(Set<RelationType> relationtypes) {
        this.relationtypes = relationtypes
    }

    Set<ItemType> getItemtypes() {
        return itemtypes
    }

    void setItemtypes(Set<ItemType> itemtypes) {
        this.itemtypes = itemtypes
    }
}
