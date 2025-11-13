package com.company.statemachine.screen.state;

import io.jmix.ui.screen.*;
import com.company.statemachine.entity.State;

@UiController("State.browse")
@UiDescriptor("state-browse.xml")
@LookupComponent("statesTable")
public class StateBrowse extends StandardLookup<State> {
}
