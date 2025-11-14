package com.company.statemachine.screen.statemachinetemplate;

import com.company.statemachine.entity.State;
import com.company.statemachine.entity.StateMachineTemplate;
import com.company.statemachine.entity.Transition;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.BrowserFrame;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("StateMachineTemplate.visualEdit")
@UiDescriptor("state-machine-template-visual-edit.xml")
@EditedEntityContainer("stateMachineTemplateDc")
public class StateMachineTemplateVisualEdit extends StandardEditor<StateMachineTemplate> {

    @Autowired
    private BrowserFrame diagramFrame;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        updateDiagram();
    }

    @Subscribe("refreshDiagramBtn")
    public void onRefreshDiagramBtnClick(Button.ClickEvent event) {
        updateDiagram();
    }

    private void updateDiagram() {
        StateMachineTemplate template = getEditedEntity();

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }");
        html.append(".canvas { background: white; border: 1px solid #ddd; min-height: 500px; position: relative; }");
        html.append(".state { position: absolute; width: 120px; height: 60px; border: 2px solid #4CAF50; ");
        html.append("border-radius: 10px; background: white; display: flex; align-items: center; ");
        html.append("justify-content: center; font-weight: bold; cursor: move; }");
        html.append(".state.final { border-color: #f44336; background: #ffebee; }");
        html.append(".state.initial { border-color: #2196F3; background: #e3f2fd; }");
        html.append("svg { position: absolute; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none; }");
        html.append(".arrow { stroke: #666; stroke-width: 2; fill: none; marker-end: url(#arrowhead); }");
        html.append(".transition-label { font-size: 12px; fill: #333; }");
        html.append("</style>");
        html.append("</head><body>");
        html.append("<h2>State Machine: ").append(escapeHtml(template.getName())).append("</h2>");

        if (template.getDescription() != null) {
            html.append("<p>").append(escapeHtml(template.getDescription())).append("</p>");
        }

        html.append("<div class='canvas' id='canvas'>");
        html.append("<svg id='svg'>");
        html.append("<defs><marker id='arrowhead' markerWidth='10' markerHeight='10' refX='9' refY='3' orient='auto'>");
        html.append("<polygon points='0 0, 10 3, 0 6' fill='#666' /></marker></defs>");

        // Draw transitions
        if (template.getTransitions() != null) {
            for (Transition t : template.getTransitions()) {
                State from = t.getFromState();
                State to = t.getToState();
                if (from != null && to != null) {
                    int x1 = from.getPositionX() != null ? from.getPositionX() + 60 : 100;
                    int y1 = from.getPositionY() != null ? from.getPositionY() + 30 : 100;
                    int x2 = to.getPositionX() != null ? to.getPositionX() + 60 : 300;
                    int y2 = to.getPositionY() != null ? to.getPositionY() + 30 : 100;

                    html.append("<line class='arrow' x1='").append(x1).append("' y1='").append(y1)
                        .append("' x2='").append(x2).append("' y2='").append(y2).append("'/>");

                    int midX = (x1 + x2) / 2;
                    int midY = (y1 + y2) / 2;
                    html.append("<text class='transition-label' x='").append(midX).append("' y='").append(midY)
                        .append("'>").append(escapeHtml(t.getEventName())).append("</text>");
                }
            }
        }

        html.append("</svg>");

        // Draw states
        if (template.getStates() != null) {
            int position = 0;
            for (State state : template.getStates()) {
                int x = state.getPositionX() != null ? state.getPositionX() : 50 + (position % 3) * 200;
                int y = state.getPositionY() != null ? state.getPositionY() : 50 + (position / 3) * 150;

                String cssClass = "state";
                if (state.getIsFinal() != null && state.getIsFinal()) {
                    cssClass += " final";
                } else if (template.getInitialStateId() != null && template.getInitialStateId().equals(state.getId())) {
                    cssClass += " initial";
                }

                html.append("<div class='").append(cssClass).append("' style='left:").append(x)
                    .append("px;top:").append(y).append("px;'>")
                    .append(escapeHtml(state.getName())).append("</div>");
                position++;
            }
        }

        html.append("</div>");
        html.append("</body></html>");

        diagramFrame.setSource(BrowserFrame.StreamSource.class)
            .setStreamSupplier(() -> new java.io.ByteArrayInputStream(html.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8)))
            .setMimeType("text/html");
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
