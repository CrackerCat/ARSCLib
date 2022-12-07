package com.reandroid.lib.arsc.chunk.xml;

import com.reandroid.lib.arsc.chunk.ChunkType;
import com.reandroid.lib.arsc.item.ResXmlString;

abstract class ResXmlNamespace<PAIR extends ResXmlNamespace> extends BaseXmlChunk{
    private PAIR mPair;
    ResXmlNamespace(ChunkType chunkType) {
        super(chunkType, 0);
    }
    @Override
    public String getUri(){
        return getString(getUriReference());
    }
    public void setUri(String uri){
        ResXmlString xmlString = getOrCreateString(uri);
        if(xmlString==null){
            throw new IllegalArgumentException("Null ResXmlString, add to parent element first");
        }
        setUriReference(xmlString.getIndex());
    }
    public String getPrefix(){
        return getString(getPrefixReference());
    }
    public void setPrefix(String prefix){
        ResXmlString xmlString = getOrCreateString(prefix);
        if(xmlString==null){
            throw new IllegalArgumentException("Null ResXmlString, add to parent element first");
        }
        setPrefixReference(xmlString.getIndex());
    }
    public int getUriReference(){
        return getStringReference();
    }
    public void setUriReference(int ref){
        setStringReference(ref);
        PAIR pair=getPair();
        if(pair!=null && pair.getUriReference()!=ref){
            pair.setUriReference(ref);
        }
    }
    public int getPrefixReference(){
        return getNamespaceReference();
    }
    public void setPrefixReference(int ref){
        setNamespaceReference(ref);
        PAIR pair=getPair();
        if(pair!=null && pair.getPrefixReference()!=ref){
            pair.setPrefixReference(ref);
        }
    }
    PAIR getPair(){
        return mPair;
    }
    void setPair(PAIR pair){
        if(pair==this){
            return;
        }
        this.mPair=pair;
        if(pair !=null && pair.getPair()!=this){
            pair.setPair(this);
        }
    }
    @Override
    public String toString(){
        String uri=getUri();
        if(uri==null){
            return super.toString();
        }
        return "xmlns:"+getPrefix()+"=\""+getUri()+"\"";
    }
}