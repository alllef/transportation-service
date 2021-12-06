package com.github.alllef.transportationservice.ui;

import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.service.TransportService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.Map;

@Route("")
@PWA(shortName = "transport-serv", name = "Transportation service")
@RequiredArgsConstructor
public class MainView extends VerticalLayout {
    private Grid<Transport> grid = new Grid<>(Transport.class);

    private final TransportService transportService;
    ComboBox<Transport> comboBox = new ComboBox<>("Transport");

    @PostConstruct
    public void afterConstruct() {
        addClassName("list-view");
        setSizeFull();
        configureComboBox();
        add(comboBox);
    }

    private void configureComboBox() {
        comboBox.setItems(transportService.findAll());
        comboBox.setItemLabelGenerator(Transport::getName);
        comboBox.addValueChangeListener(event ->add());
    }
}