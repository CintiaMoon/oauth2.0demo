package com.hxgis.authserver.controller;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for retrieving the model for and displaying the confirmation page for access to a protected resource.
 * 
 * @author Ryan Heaton
 */
@Controller
@SessionAttributes("authorizationRequest")
public class AccessConfirmationController {

	private ClientDetailsService clientDetailsService;
	private ApprovalStore approvalStore;

	@RequestMapping("/oauth/confirm_access")
	public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {

		AuthorizationRequest clientAuth = (AuthorizationRequest) model.remove("authorizationRequest");
		ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
		model.put("auth_request", clientAuth);
		model.put("client", client);

		model.put("denial", false);

		if (request.getAttribute("_csrf") != null) {
			model.put("_csrf", request.getAttribute("_csrf"));
		}

		if (model.containsKey("scopes") || request.getAttribute("scopes") != null) {
			model.put("scopeList", createScopeList(model, request));
		} else {
			model.put("scopes", null);
			model.put("denial", true);
		}


		return new ModelAndView("access_confirmation", model);
	}

	private List<Map<String,Object>> createScopeList(Map<String, Object> model, HttpServletRequest request) {
		List<Map<String,Object>> scopeList = new ArrayList<>();
		@SuppressWarnings("unchecked")
		Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ? model.get("scopes") : request
				.getAttribute("scopes"));
		for (String scope : scopes.keySet()) {
			String approved = "true".equals(scopes.get(scope)) ? " checked" : "";
			String denied = !"true".equals(scopes.get(scope)) ? " checked" : "";

			Map<String,Object> map = new HashMap<>();
			map.put("scope",scope);
			map.put("key",scope);
			map.put("approved", approved);
			map.put("denied",denied);

			scopeList.add(map);
		}
		return scopeList;
	}

	@RequestMapping("/oauth/error")
	public String handleError(Map<String, Object> model) throws Exception {
		// We can add more stuff to the model here for JSP rendering. If the client was a machine then
		// the JSON will already have been rendered.
		model.put("message", "There was a problem with the OAuth2 protocol");
		return "oauth_error";
	}

	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}

	public void setApprovalStore(ApprovalStore approvalStore) {
		this.approvalStore = approvalStore;
	}
}
