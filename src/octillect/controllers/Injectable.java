package octillect.controllers;

/**
 * Inject a Parent Controller in the controller that implements this interface.
 *
 * @param <ParentController> the controller that needs to be injected.
 */
public interface Injectable<ParentController> {
    void inject(ParentController parentController);
    void init();
}
