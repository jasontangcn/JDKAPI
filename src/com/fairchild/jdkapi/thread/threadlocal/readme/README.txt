ThreadLocal和InheritableThreadLocal

1、JDK1.2添加了线程局部变量的支持。
2、ThreadLocal包含一个WeakHashMap的引用，它保存有键值对。
3、JDK1.2中引入弱引用。
   WeakHashMap利用弱引用自动删除已经消亡、并在其它地方消除引用的线程映射。
4、在WeakHashMap中，查找键是Thread的引用，保存的值是一个ThreadLocal.Entry对象。
   ThreadLocal.Entry是ThreadLocal的一个内部类，ThreadLocal使用它来保存特定线程值。
5、InheritableThreadLocal是ThreadLocal的子类，它提供了让特定线程变量从父线程继承到子线程的机制。
6、InheritableThreadLocal.Entry是ThreadLocal.Entry的一个子类，而且也是一个内部类。
Thread包含InhertiableThread.Entry对象的一个私有引用，创建新线程时，使用它来将特定线程变量传递到子线程。


