<?php

namespace Japode\Widget;

class AbstractWidget {

    private $atribbuteName;
    public $loader;
    public $twig;
    public $template;

    function __construct() {
        $this->loader = new \Twig_Loader_Filesystem('jp-views');
        $this->twig = new \Twig_Environment($this->loader);
    }

    function setAttributeName($_attributeName) {
        $this->atribbuteName = $_attributeName;
    }

    function getAttributeName() {
        return $this->atribbuteName;
    }

    /*
     * Called by the portlet container to indicate to a portlet that the 
     * portlet is being taken out of service.
     */

    function destroy() {
        
    }

    /*
     * The default implementation of this method routes the render request to:
     * method annotated with @RenderMode and the name of the portlet mode a set
     * of helper methods depending on the current portlet mode the portlet is
     * currently in.

     */

    function doDispatch(RenderRequest $request, RenderResponse $response) {
        
    }

    /*
     * Helper method to serve up the edit mode.
     * 
     */

    function doEdit() {
        
    }

    /*
     * Used by the render method to set the response properties and headers.
     */

    function doHeaders(RenderRequest $request, RenderResponse $response) {
        
    }

    /*
     * Helper method to serve up the help mode.
     */

    protected function doHelp(RenderRequest $request, RenderResponse $response) {
        
    }

    /*
     * Helper method to serve up the mandatory view mode.
     */

    protected function doView(RenderRequest $request, RenderResponse $response) {
        
    }

    /*
     * Returns a String containing the value of the named initialization * 
     * parameter, or null if the parameter does not exist.
     */

    function getInitParameter($name) {
        
    }

    /*
     * Returns the names of the portlet initialization parameters as an 
     * Enumeration of String objects, or an empty Enumeration if the portlet 
     * has no initialization parameters.
     */

    function getInitParameterNames() {
        
    }

    /*
     * Used by the render method to set the next possible portlet modes.
     */

    function getNextPossiblePortletModes(RenderRequest $request) {
        
    }

    /*
     * Returns the PortletConfig object of this portlet.
     */

    function getPortletConfig() {
        
    }

    /*
     * Returns the PortletContext of the portlet application the portlet is in.
     */

    function getPortletContext() {
        
    }

    /*
     *       Returns the name of this portlet.
     */

    function getPortletName() {
        
    }

    /*
     * Returns the QNames of the processing events supported by the portlet as
     * an Enumeration of QName objects, or an empty Enumeration if the portlet
     * has not defined any processing events.
     */

    function getProcessingEventQNames() {
        
    }

    /*
     *  Returns the names of the public render parameters supported by the
     * portlet as an Enumeration of String objects, or an empty Enumeration if 
     * the portlet has no public render parameters.
     */

    function getPublicRenderParameterNames() {
        
    }

    /*
     * Gets the resource bundle for the given locale based on the resource
     * bundle defined in the deployment descriptor with resource-bundle tag or
     * the inlined resources defined in the deployment descriptor.
     */

    function getResourceBundle(Locale $locale) {
        
    }

    /*
     * Returns the locales supported by the portlet as an Enumeration of
     * Locale objects, or an empty Enumeration if the portlet has not defined
     * any supported locales.
     */

    function getSupportedLocales() {
        
    }

    /*
     *      Used by the render method to get the title.
     */

    function getTitle(RenderRequest $request) {
        
    }

    /*
     * Called by the portlet container to indicate to a portlet that the portlet
     * is being placed into service.
     */

    function init(PortletConfig $config) {
        
    }

    /*
     * Called by the portlet container to allow the portlet to process an action
     * request.
     */

    function processAction() {
        
    }

    /*
     * The default implementation tries to dispatch to a method annotated with 
     * @ProcessEvent that matches the event name or, if no such method is found 
     * just sets the current render parameters on the response.
     * Note that the annotated methods needs to be public in order to be allowed
     * to be called by GenericPortlet.
     */

    function processEvent(EventRequest $request, EventResponse $response) {
        
    }

    /*
     * The default implementation of this method sets the headers using the
     * doHeaders method, sets the title using the getTitle method and invokes 
     * the doDispatch method.
     */

    function render(RenderRequest $request, RenderResponse $response) {
        
    }

    /*
     * Default resource serving.
     */

    function serveResource(ResourceRequest $request, ResourceResponse $response) {
        
    }

}
