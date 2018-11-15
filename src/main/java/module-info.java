module space.util {
	
	exports space.util;
	exports space.util.annotation;
	exports space.util.sync.barrier;
	exports space.util.baseobject;
	exports space.util.baseobject.exceptions;
	exports space.util.buffer;
	exports space.util.buffer.array;
	exports space.util.buffer.direct;
	exports space.util.buffer.direct.alloc;
	exports space.util.buffer.direct.alloc.stack;
	exports space.util.buffer.exception;
	exports space.util.buffer.pointer;
	exports space.util.buffer.string;
//	exports space.util.delegate;
	exports space.util.delegate.collection;
	exports space.util.delegate.indexmap;
	exports space.util.delegate.indexmap.entry;
	exports space.util.delegate.iterator;
	exports space.util.delegate.list;
	exports space.util.delegate.list.listiterator;
	exports space.util.delegate.map;
	exports space.util.delegate.map.entry;
	exports space.util.delegate.map.specific;
	exports space.util.delegate.set;
	exports space.util.delegate.specific;
	exports space.util.delegate.util;
	exports space.util.dependency;
	exports space.util.dependency.exception;
	exports space.util.event;
	//NOCOMMIT: comment out
//	exports space.util.event.dependency;
	exports space.util.event.typehandler;
	exports space.util.freeableStorage;
	exports space.util.future;
	exports space.util.gui;
//	exports space.util.gui.elements;
	exports space.util.gui.elements.direction;
	exports space.util.gui.elements.text;
	exports space.util.gui.exception;
	exports space.util.gui.monofont;
//	exports space.util.gui.monofont.elements;
	exports space.util.gui.monofont.elements.direction;
	exports space.util.gui.monofont.elements.text;
	exports space.util.gui.monofont.tableCreator;
	exports space.util.gui.monofont.tableCreator.multi;
	exports space.util.gui.monofont.tsh;
	exports space.util.gui.monofont.tsh.elements;
	exports space.util.gui.monofont.tsh.objectsCreator;
	exports space.util.gui.tsh;
	exports space.util.gui.tsh.elements;
	exports space.util.indexmap;
	exports space.util.indexmap.axis;
	exports space.util.indexmap.multi;
	exports space.util.key;
	exports space.util.key.attribute;
	exports space.util.key.impl;
	exports space.util.key.map;
//	exports space.util.lock;
	exports space.util.lock.keylock;
	exports space.util.lock.simplelock;
	exports space.util.logger;
	exports space.util.logger.prefix;
	exports space.util.logger.printer;
	exports space.util.math;
	exports space.util.methodhandle;
//	exports space.util.methodhandle.capsule; //do NOT export
	exports space.util.primitive;
	exports space.util.stack;
	exports space.util.stack.multistack;
	exports space.util.string;
	exports space.util.string.builder;
	exports space.util.string.toStringHelper;
	exports space.util.task;
	exports space.util.task.impl;
//	exports space.util.unsafe; //do NOT export
	
	requires jdk.unsupported;
	
	requires annotations;
}