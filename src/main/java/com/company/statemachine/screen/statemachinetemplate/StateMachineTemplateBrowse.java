package com.company.statemachine.screen.statemachinetemplate;

import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.screen.*;
import com.company.statemachine.entity.StateMachineTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("StateMachineTemplate.browse")
@UiDescriptor("state-machine-template-browse.xml")
@LookupComponent("stateMachineTemplatesTable")
public class StateMachineTemplateBrowse extends StandardLookup<StateMachineTemplate> {

    @Autowired
    private GroupTable<StateMachineTemplate> stateMachineTemplatesTable;

    @Autowired
    private ScreenBuilders screenBuilders;

    @Subscribe("stateMachineTemplatesTable.visualEdit")
    public void onStateMachineTemplatesTableVisualEdit(Action.ActionPerformedEvent event) {
        StateMachineTemplate selected = stateMachineTemplatesTable.getSingleSelected();
        if (selected != null) {
            screenBuilders.editor(stateMachineTemplatesTable)
                .withScreenClass(StateMachineTemplateVisualEdit.class)
                .editEntity(selected)
                .build()
                .show();
        }
    }
}
