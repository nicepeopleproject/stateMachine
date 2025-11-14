package com.company.statemachine.screen.login;

import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

@Route(path = "login", root = true)
@UiController("LoginScreen")
@UiDescriptor("login-screen.xml")
public class LoginScreen extends io.jmix.ui.screen.LoginScreen {
}
