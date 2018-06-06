package pro2e;

public class TraceV1 {
	private String className;
	private String hashCode;
	private static boolean show = false; // On / Off aller Meldungen
	private static boolean showBuild = false; // Zeigt nur Attribute & Konstruktor
	private static boolean showMethod = true; // Zeigt nur Attribute & Konstruktor
	private boolean showObject = false; // Zeigt alle Aufrufe eines Objekts

	// Some virtual machines may, under some circumstances, omit one or more
	// stack frames from the stack trace. In the extreme case, a virtual machine
	// that has no stack trace information concerning this thread is permitted
	// to return a zero-length array from this method.

	public TraceV1(String className, int hashCode, boolean showObject) {
		this.className = className;
		this.hashCode = "" + hashCode;
		this.showObject = showObject;
		if (show && (showBuild || showObject)) {
			System.out.println("Attribute von " + className + "@" + hashCode + " werden initialisiert ...");
		}
	}

	public TraceV1(String className, int hashCode) {
		this.className = className;
		this.hashCode = "" + hashCode;
		if (show && (showBuild || showObject)) {
			System.out.println("Attribute von " + className + "@" + hashCode + " werden initialisiert ...");
		}
	}

	public TraceV1(Object obj, boolean showObject) {
		this.className = obj.getClass().getName();
		this.hashCode = "" + obj.hashCode();
		this.showObject = showObject;
		if (show && (showBuild || showObject)) {
			System.out.println("Attribute von " + className + "@" + hashCode + " werden initialisiert ...");
		}
	}

	public TraceV1(Object obj) {
		this.className = obj.getClass().getName();
		this.hashCode = "" + obj.hashCode();
		if (show && (showBuild || showObject)) {
			System.out.println("Attribute von " + className + "@" + hashCode + " werden initialisiert ...");
		}
	}

	public TraceV1(String className, Object obj, boolean showObject) {
		this.className = className;
		this.hashCode = "" + obj.hashCode();
		this.showObject = showObject;
		if (show && (showBuild || showObject)) {
			System.out.println("Attribute von " + className + "@" + hashCode + " werden initialisiert ...");
		}
	}

	public TraceV1(String className, Object obj) {
		this.className = className;
		this.hashCode = "" + obj.hashCode();
		if (show && (showBuild || showObject)) {
			System.out.println("Attribute von " + className + "@" + hashCode + " werden initialisiert ...");
		}
	}

	public TraceV1() {
	}

	public static void mainCall() {
		System.out.println("Start via main(String args[])");
	}

	public static void mainCall(boolean show, boolean showBuild, boolean showMethode) {
		new TraceV1();
		TraceV1.show = show;
		TraceV1.showBuild = showBuild;
		TraceV1.showMethod = showMethode;

		System.out.println("Start via main(String args[])");
	}

	public void constructorCall() {
		if (show && (showBuild || showObject)) {
			System.out
					.println("Konstruktor " + className + "():" + className + "@" + hashCode + " wird ausgeführt ...");
		}
	}

	public void constructorCall(String constructor) {
		if (show && (showBuild || showObject))
			System.out
					.println("Konstruktor " + constructor + ":" + className + "@" + hashCode + " wird ausgeführt ...");
	}

	public void methodeCall() {
		String methode = getMethodName(3);
		if (show && (showMethod || showObject))
			System.out.println("Methode " + className + "@" + hashCode + "." + methode + "()" + " wird ausgeführt ...");
	}

	public void methodeCall(String methode) {
		if (show && (showMethod || showObject))
			System.out.println("Methode " + className + "@" + hashCode + "." + methode + " wird ausgeführt ...");
	}

	public void eventCall() {
		String methode = getMethodName(3);
		if (show && (showMethod || showObject))
			System.out.println("\nEvent:\n" + "Methode " + className + "@" + hashCode + "." + methode + "()"
					+ " wird durch Ereignis ausgelöst ...");
	}

	public void eventCall(String methode) {
		if (show && (showMethod || showObject))
			System.out.println("\nEvent:\n" + "Methode " + className + "@" + hashCode + "." + methode
					+ " wird durch Ereignis ausgelöst ...");
	}

	public void registerObserver(Object observable, Object observer) {
		String classNameObservable = observable.getClass().getName();
		String hashCodeObservable = "" + observable.hashCode();
		String classNameObserver = observer.getClass().getName();
		String hashCodeObserver = "" + observer.hashCode();
		if (show && (showMethod || showObject)) {
			System.out.println("Objekt " + classNameObserver + "@" + hashCodeObserver + " wird als Observer von "
					+ "Objekt " + classNameObservable + "@" + hashCodeObservable + " registriert!");
		}
	}

	public static String getMethodName(int depth) {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();

		// for (int i = 0; i < ste.length; i++) {
		// System.out.println(ste[i].getMethodName());
		// }

		if (ste != null)
			return ste[depth].getMethodName();

		return "VM does not provied a Stack-Trace";
	}

}