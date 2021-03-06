/*
 * Created on Mar 9, 2005
 */
package dguitar.players.sound.midi;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;

import dguitar.players.sound.PerformanceEvent;
import dguitar.players.sound.PerformanceTimerEvent;
import dguitar.players.sound.PerformanceTimerListener;


/**
 * @author crnash
 */
public class MidiPerformanceTimerListenerProxy implements MetaPerformer, MetaEventListener
{
    PerformanceTimerListener listener;
    MetaContainer container;
    
    /**
     * @param listener
     */
    public MidiPerformanceTimerListenerProxy(PerformanceTimerListener listener)
    {
        this.listener=listener;
        this.container=null;
    }
    
    /* (non-Javadoc)
     * @see javax.sound.midi.MetaEventListener#meta(javax.sound.midi.MetaMessage)
     */
    public final void meta(MetaMessage meta)
    {
        if(meta.getType()==0x01)
        {
            byte[] data=meta.getData();
            if(data.length==4)
            {
	            int key=
	                ((data[0] & 0x00FF)<<24)|
	                ((data[1] & 0x00FF)<<16)|
	                ((data[2] & 0x00FF)<< 8)|
	                ((data[3] & 0x00FF)    );
	            PerformanceEvent item=container.lookup(key);
	            if(item!=null)
	            {
	                if(item instanceof PerformanceTimerEvent)
	                {
	                    listener.onTimer((PerformanceTimerEvent)item);
	                }
	            }
            }
        }
    }
    /**
     * @param container The container to set.
     */
    public void setContainer(MetaContainer container)
    {
        this.container = container;
    }
}
