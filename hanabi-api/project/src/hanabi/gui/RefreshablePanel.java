package hanabi.gui;


import javax.swing.*;
import java.awt.*;

public abstract class RefreshablePanel<T> extends JPanel
{
	private boolean inited=false;

	public final String name;

	private T model;
	private final Object modelmonitor = new Object();

	public RefreshablePanel(String name)
	{
		if (name == null || name.equals(""))
			name = this.getClass().getName();
		this.name = name;
//		StaticLogger.log(name+" ("+this.getClass().getPlayer()+") created");
	}

	public Component add(Component comp)
	{
		if (inited)
			throw new IllegalStateException("An inited panel can not add components!");
		Component ret = super.add(comp);
		return ret;
	}

	public void add(Component comp, Object constraints)
	{
		if (inited)
			throw new IllegalStateException("An inited panel can not add components!");
		super.add(comp, constraints);
	}

	protected abstract void afterChildrenInit();

	protected abstract void afterChildrenRefresh(T model);

	protected abstract void beforeChildrenInit();

	protected abstract void beforeChildrenRefresh(T model);

	public T getModel()
	{
		synchronized (modelmonitor)
		{
			return model;
		}
	}

	public final void init()
	{
		if (inited)
			throw new IllegalStateException("Already inited");

//		StaticLogger.log("Initiating "+name);
		beforeChildrenInit();
		for (Component c :getComponents())
		{
			if (c instanceof RefreshablePanel)
				((RefreshablePanel)c).init();
		}
		afterChildrenInit();
		inited = true;
	}

	public boolean isInited()
	{
		return inited;
	}

	public final void refresh()
	{
		synchronized (modelmonitor)
		{
			beforeChildrenRefresh(model);
			for (Component c :getComponents())
			{
				if (c instanceof RefreshablePanel)
					((RefreshablePanel)c).refresh();
			}
			afterChildrenRefresh(model);
		}
//		StaticLogger.log(name+" refreshed");
	}

	public void setModel(T newmodel)
	{
		synchronized (modelmonitor)
		{
			model = newmodel;
		}
	}
}
