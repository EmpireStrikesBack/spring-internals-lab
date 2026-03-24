package com.example;

/*
 * Level 4 : Mini DI — main entry point
 *
 * Exercises the container in a progressive sequence that mirrors
 * the step-by-step build in the documentation:
 *
 *   Step 3 : scanner demo (including NotAComponent rejection)
 *   Step 5 : full container lifecycle (scan → create → inject)
 *   Step 6 : @PostConstruct confirmed via AppController.onReady()
 *   Step 7 : direct bean retrieval and method calls
 *   Step 8 : circular dependency detection (expected failure + recovery)
 */
public class App {

    public static void main(String[] args) throws Exception {

        // ── Step 3 : ComponentScanner demo ──────────────────────────────
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  STEP 3 : ComponentScanner demo          ║");
        System.out.println("╚══════════════════════════════════════════╝");
        ComponentScanner scanner = new ComponentScanner();
        scanner.scan(
                UserRepository.class,
                UserService.class,
                AppController.class,
                NotAComponent.class   // should be skipped
        );

        // ── Step 5 : Full container lifecycle ───────────────────────────
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║  STEP 5 : Full container lifecycle       ║");
        System.out.println("╚══════════════════════════════════════════╝");
        MiniApplicationContext context = new MiniApplicationContext();
        context.register(
                UserRepository.class,
                UserService.class,
                AppController.class
        );

        // ── Step 6 : Use beans retrieved from context ────────────────────
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  STEP 6 : Bean retrieval & method calls  ║");
        System.out.println("╚══════════════════════════════════════════╝");
        AppController controller = context.getBean(AppController.class);
        controller.handleRequest();

        UserService service = context.getBean(UserService.class);
        System.out.println("\nDirect service call : " + service.getUser(2));

        // ── Step 7 : Singleton guarantee ─────────────────────────────────
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║  STEP 7 : Singleton guarantee            ║");
        System.out.println("╚══════════════════════════════════════════╝");
        AppController c1 = context.getBean(AppController.class);
        AppController c2 = context.getBean(AppController.class);
        System.out.println("c1 == c2 : " + (c1 == c2));  // must be true

        // ── Step 8 : Circular field-injection — resolves with two-phase ──
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║  STEP 8 : Circular field-injection (two-phase)  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        // Why field cycles resolve:
        //   Phase 2 : CircularA created (empty), CircularB created (empty)
        //   Phase 3 : A.b = CircularB instance  →  B.a = CircularA instance
        //   Both objects exist before wiring starts, so no chicken-and-egg.
        //
        // Why constructor cycles always fail:
        //   new CircularA(b) needs a CircularB to pass.
        //   new CircularB(a) needs a CircularA to pass.
        //   Neither can be created first — JVM deadlock at instantiation.
        //
        // Spring's behaviour is identical: field/setter injection tolerates
        // cycles; constructor injection does not and throws
        // BeanCurrentlyInCreationException.
        System.out.println("[Note] Phase 2 creates both beans before Phase 3 wires either.");
        System.out.println("[Note] Field-injection cycles resolve; constructor cycles do not.");
        MiniApplicationContext circularContext = new MiniApplicationContext();
        circularContext.register(CircularA.class, CircularB.class);
        CircularA ca = circularContext.getBean(CircularA.class);
        CircularB cb = circularContext.getBean(CircularB.class);
        System.out.println("CircularA resolved: " + (ca != null));
        System.out.println("CircularB resolved: " + (cb != null));
        System.out.println("[OK] Circular field-injection resolved — same as Spring default.");

        // ── Container summary ─────────────────────────────────────────────
        context.printSummary();
    }
}