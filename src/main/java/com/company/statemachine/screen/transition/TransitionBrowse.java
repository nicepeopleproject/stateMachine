package com.company.statemachine.screen.transition;

import io.jmix.ui.screen.*;
import com.company.statemachine.entity.Transition;

@UiController("Transition.browse")
@UiDescriptor("transition-browse.xml")
@LookupComponent("transitionsTable")
public class TransitionBrowse extends StandardLookup<Transition> {
}
