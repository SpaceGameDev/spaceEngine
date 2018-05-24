package space.engine.window;

import org.jetbrains.annotations.NotNull;
import space.util.baseobject.Freeable;
import space.util.key.attribute.AttributeListCreator.IAttributeList;

/**
 * The {@link WindowFramework} is the first Interface you interact with when opening any {@link Window Windows}.
 * <ul>
 * <li>Get all {@link WindowFrameworkCreator} by calling {@link WindowFramework#getAvailableFrameworks()}</li>
 * <li>Select one and call {@link WindowFrameworkCreator#createFramework()} to initialize it</li>
 * <li>Create a context with {@link WindowFramework#createContext(IAttributeList) createContext(IAttributeList)} and a properly filled {@link WindowContext#CREATOR AttributeList form the WindowContext}</li>
 * <li>You can also query {@link WindowFramework#getPrimaryMonitor()} or {@link WindowFramework#getAllMonitors()}</li>
 * </ul>
 */
public interface WindowFramework extends Freeable {
	
	@NotNull WindowContext createContext(@NotNull IAttributeList<WindowContext> format);
	
	@NotNull Monitor[] getAllMonitors();
	
	@NotNull Monitor getPrimaryMonitor();
	
	@NotNull
	static WindowFrameworkCreator[] getAvailableFrameworks() {
		return new WindowFrameworkCreator[] {};
	}
}