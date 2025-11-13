package com.company.statemachine.screen.transition;

import io.jmix.ui.screen.*;
import com.company.statemachine.entity.Transition;

@UiController("Transition.edit")
@UiDescriptor("transition-edit.xml")
@EditedEntityContainer("transitionDc")
public class TransitionEdit extends StandardEditor<Transition> {
}
