package com.company.statemachine.screen.state;

import io.jmix.ui.screen.*;
import com.company.statemachine.entity.State;

@UiController("State.edit")
@UiDescriptor("state-edit.xml")
@EditedEntityContainer("stateDc")
public class StateEdit extends StandardEditor<State> {
}
