package gr.cti.eslate.mediaPlayer;

import gr.cti.eslate.utils.ESlateBeanInfo;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.swing.ImageIcon;

/**
 * BeanInfo for Media player component.
 * 
 * @author augril
 */
public class MediaPlayerBeanInfo extends ESlateBeanInfo {
	/**
	 * Construct the BeanInfo.
	 */
	public MediaPlayerBeanInfo() {
		super();
		try {
			set16x16ColorIcon(new ImageIcon(getClass().getResource(
					"images/media.gif")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the bean's <code>PropertyDescriptor</code>s.
	 * 
	 * @return An array of PropertyDescriptors describing the editable
	 *         properties supported by this bean.
	 */
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor propertyDescriptor1 = new PropertyDescriptor(
					"file", MediaPlayer.class, "getFile", "setFile");
			propertyDescriptor1.setDisplayName(BundleMessages
					.getMessage("fileProperty"));
			propertyDescriptor1.setShortDescription(BundleMessages
					.getMessage("filePropertyTip"));

			PropertyDescriptor[] propertyDescriptors = new PropertyDescriptor[1];
			int i = 0;
			propertyDescriptors[i++] = propertyDescriptor1;
			return propertyDescriptors;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the bean's <code>EventSetDescriptor</code>s.
	 * 
	 * @return An array of EventSetDescriptors describing the kinds of events
	 *         fired by this bean.
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		try {
			Method listenerMethod1 = MediaPlayerListener.class.getMethod(
					"playerStarted", new Class[] { MediaPlayerEvent.class });
			Method listenerMethod2 = MediaPlayerListener.class.getMethod(
					"playerPaused", new Class[] { MediaPlayerEvent.class });
			Method listenerMethod3 = MediaPlayerListener.class.getMethod(
					"playerStopped", new Class[] { MediaPlayerEvent.class });
			Method listenerMethod4 = MediaPlayerListener.class.getMethod(
					"playerFinished", new Class[] { MediaPlayerEvent.class });
			Method addMediaPlayerListenerMethod = MediaPlayer.class.getMethod(
					"addMediaPlayerListener",
					new Class[] { MediaPlayerListener.class });
			Method removeMediaPlayerListenerMethod = MediaPlayer.class
					.getMethod("removeMediaPlayerListener",
							new Class[] { MediaPlayerListener.class });

			MethodDescriptor methodDescriptor1 = new MethodDescriptor(
					listenerMethod1);
			methodDescriptor1.setDisplayName(BundleMessages
					.getMessage("playerStartedEvent"));
			MethodDescriptor methodDescriptor2 = new MethodDescriptor(
					listenerMethod2);
			methodDescriptor2.setDisplayName(BundleMessages
					.getMessage("playerPausedEvent"));
			MethodDescriptor methodDescriptor3 = new MethodDescriptor(
					listenerMethod3);
			methodDescriptor3.setDisplayName(BundleMessages
					.getMessage("playerStoppedEvent"));
			MethodDescriptor methodDescriptor4 = new MethodDescriptor(
					listenerMethod4);
			methodDescriptor4.setDisplayName(BundleMessages
					.getMessage("playerFinishedEvent"));
			EventSetDescriptor eventSetDescriptor1 = new EventSetDescriptor(
					"player", MediaPlayerListener.class,
					new MethodDescriptor[] { methodDescriptor1,
							methodDescriptor2, methodDescriptor3,
							methodDescriptor4 }, addMediaPlayerListenerMethod,
					removeMediaPlayerListenerMethod);

			EventSetDescriptor[] eventSetDescriptorsSuper = super.getEventSetDescriptors();
			EventSetDescriptor[] eventSetDescriptors = new EventSetDescriptor[1+eventSetDescriptorsSuper.length];
			int i = 0;
			for (int j = 0; j < eventSetDescriptorsSuper.length; j++)
				eventSetDescriptors[i++] = eventSetDescriptorsSuper[j];
			eventSetDescriptors[i++] = eventSetDescriptor1;
			return eventSetDescriptors;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
