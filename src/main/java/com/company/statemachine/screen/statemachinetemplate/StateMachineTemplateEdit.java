package com.company.statemachine.screen.statemachinetemplate;

import io.jmix.ui.screen.*;
import com.company.statemachine.entity.StateMachineTemplate;

@UiController("StateMachineTemplate.edit")
@UiDescriptor("state-machine-template-edit.xml")
@EditedEntityContainer("stateMachineTemplateDc")
public class StateMachineTemplateEdit extends StandardEditor<StateMachineTemplate> {
}
