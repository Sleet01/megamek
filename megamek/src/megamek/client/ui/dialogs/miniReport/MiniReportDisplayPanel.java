/*
 * Copyright (C) 2000-2002 Ben Mazur (bmazur@sev.org)
 * Copyright (C) 2025 The MegaMek Team. All Rights Reserved.
 *
 * This file is part of MegaMek.
 *
 * MegaMek is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPL),
 * version 3 or (at your option) any later version,
 * as published by the Free Software Foundation.
 *
 * MegaMek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * A copy of the GPL should have been included with this project;
 * if not, see <https://www.gnu.org/licenses/>.
 *
 * NOTICE: The MegaMek organization is a non-profit group of volunteers
 * creating free software for the BattleTech community.
 *
 * MechWarrior, BattleMech, `Mech and AeroTech are registered trademarks
 * of The Topps Company, Inc. All Rights Reserved.
 *
 * Catalyst Game Labs and the Catalyst Game Labs logo are trademarks of
 * InMediaRes Productions, LLC.
 *
 * MechWarrior Copyright Microsoft Corporation. MegaMek was created under
 * Microsoft's "Game Content Usage Rules"
 * <https://www.xbox.com/en-US/developers/rules> and it is not endorsed by or
 * affiliated with Microsoft.
 */
package megamek.client.ui.dialogs.miniReport;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

import megamek.client.Client;
import megamek.client.ui.Messages;
import megamek.client.ui.clientGUI.GUIPreferences;
import megamek.client.ui.clientGUI.IClientGUI;
import megamek.client.ui.clientGUI.IHasBoardView;
import megamek.client.ui.dialogs.unitDisplay.IHasUnitDisplay;
import megamek.client.ui.util.BASE64ToolKit;
import megamek.client.ui.util.KeyCommandBind;
import megamek.client.ui.util.UIUtil;
import megamek.common.Entity;
import megamek.common.Player;
import megamek.common.Report;
import megamek.common.enums.GamePhase;
import megamek.common.event.GameListener;
import megamek.common.event.GameListenerAdapter;
import megamek.common.event.GamePhaseChangeEvent;
import megamek.common.preference.ClientPreferences;
import megamek.common.preference.PreferenceManager;

/**
 * Shows reports, with an Okay JButton
 */
public class MiniReportDisplayPanel extends JPanel implements ActionListener, HyperlinkListener {
    private JButton butSwitchLocation;
    private JTabbedPane tabs;
    private JButton butPlayerSearchUp;
    private JButton butPlayerSearchDown;
    private JButton butEntitySearchUp;
    private JButton butEntitySearchDown;
    private JButton butQuickSearchUp;
    private JButton butQuickSearchDown;
    private JButton butQuickFilter;
    private JComboBox<String> comboPlayer = new JComboBox<>();
    private JComboBox<String> comboEntity = new JComboBox<>();
    private JComboBox<String> comboQuick = new JComboBox<>();
    private IClientGUI currentClientgui;
    private Client currentClient;
    private static final GUIPreferences GUIP = GUIPreferences.getInstance();
    private static final ClientPreferences CP = PreferenceManager.getClientPreferences();

    private boolean filterEnabled = false;

    private static final int MRD_MAXNAMELENGHT = 60;

    public MiniReportDisplayPanel(IClientGUI clientgui) {

        if (clientgui == null) {
            return;
        }

        currentClientgui = clientgui;
        if (clientgui.getClient() instanceof Client) {
            currentClient = (Client) clientgui.getClient();
        } else {
            return;
        }

        currentClient.getGame().addGameListener(gameListener);
        butSwitchLocation = new JButton(Messages.getString("MiniReportDisplay.SwitchLocation"));
        butSwitchLocation.addActionListener(this);
        butPlayerSearchUp = new JButton(Messages.getString("MiniReportDisplay.ArrowUp"));
        butPlayerSearchUp.addActionListener(this);
        butPlayerSearchDown = new JButton(Messages.getString("MiniReportDisplay.ArrowDown"));
        butPlayerSearchDown.addActionListener(this);
        butEntitySearchUp = new JButton(Messages.getString("MiniReportDisplay.ArrowUp"));
        butEntitySearchUp.addActionListener(this);
        butEntitySearchDown = new JButton(Messages.getString("MiniReportDisplay.ArrowDown"));
        butEntitySearchDown.addActionListener(this);
        butQuickSearchUp = new JButton(Messages.getString("MiniReportDisplay.ArrowUp"));
        butQuickSearchUp.addActionListener(this);
        butQuickSearchUp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
              .put(KeyCommandBind.keyStroke(KeyCommandBind.REPORT_KEY_PREV), "MiniReportDisplay.ArrowUp");
        butQuickSearchUp.getActionMap().put("MiniReportDisplay.ArrowUp", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                butQuickSearchUp.doClick();
            }
        });
        butQuickSearchUp.setToolTipText(Messages.getString("MiniReportDisplay.tooltip.ArrowUp")
              + ": "
              + KeyCommandBind.getDesc(KeyCommandBind.REPORT_KEY_PREV));

        butQuickSearchDown = new JButton(Messages.getString("MiniReportDisplay.ArrowDown"));
        butQuickSearchDown.addActionListener(this);
        butQuickSearchDown.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
              .put(KeyCommandBind.keyStroke(KeyCommandBind.REPORT_KEY_NEXT), "MiniReportDisplay.ArrowDown");
        butQuickSearchDown.getActionMap().put("MiniReportDisplay.ArrowDown", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                butQuickSearchDown.doClick();
            }
        });
        butQuickSearchDown.setToolTipText(Messages.getString("MiniReportDisplay.tooltip.ArrowDown")
              + ": "
              + KeyCommandBind.getDesc(KeyCommandBind.REPORT_KEY_NEXT));

        butQuickFilter = new JButton(Messages.getString("MiniReportDisplay.KeywordFilter"));
        butQuickFilter.addActionListener(this);
        butQuickFilter.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
              .put(KeyCommandBind.keyStroke(KeyCommandBind.REPORT_KEY_FILTER), "MiniReportDisplay.KeywordFilter");
        butQuickFilter.getActionMap().put("MiniReportDisplay.KeywordFilter", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                butQuickFilter.doClick();
            }
        });
        butQuickFilter.setToolTipText(Messages.getString("MiniReportDisplay.tooltip.KeywordFilter")
              + ": "
              + KeyCommandBind.getDesc(KeyCommandBind.REPORT_KEY_FILTER));

        comboQuick.addActionListener(this);
        comboQuick.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
              .put(KeyCommandBind.keyStroke(KeyCommandBind.REPORT_KEY_SELNEXT), "MiniReportDisplay.comboQuickNext");
        comboQuick.getActionMap().put("MiniReportDisplay.comboQuickNext", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboQuick.getItemCount() > 1) {
                    comboQuick.setSelectedIndex((comboQuick.getSelectedIndex() + 1) % comboQuick.getItemCount());
                }
            }
        });
        comboQuick.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
              .put(KeyCommandBind.keyStroke(KeyCommandBind.REPORT_KEY_SELPREV), "MiniReportDisplay.comboQuickPrev");
        comboQuick.getActionMap().put("MiniReportDisplay.comboQuickPrev", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboQuick.getItemCount() > 1) {
                    int i = (comboQuick.getSelectedIndex() - 1) % comboQuick.getItemCount();
                    if (i < 0) {
                        i = comboQuick.getItemCount() - 1;
                    }
                    comboQuick.setSelectedIndex(i);
                }
            }
        });
        comboQuick.setToolTipText("<html>"
              + Messages.getString("MiniReportDisplay.tooltip.ComboQuickNext")
              + ": "
              + KeyCommandBind.getDesc(KeyCommandBind.REPORT_KEY_SELNEXT)
              + "<br>"
              + Messages.getString("MiniReportDisplay.tooltip.ComboQuickPrev")
              + ": "
              + KeyCommandBind.getDesc(KeyCommandBind.REPORT_KEY_SELPREV)
              + "<br>"
              + Messages.getString("MiniReportDisplay.tooltip.ComboQuickInfo")
              + "</html>");

        setLayout(new BorderLayout());

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(new EmptyBorder(2, 2, 15, 2));
        p.add(comboPlayer);
        p.add(butPlayerSearchUp);
        p.add(butPlayerSearchDown);
        p.add(comboEntity);
        p.add(butEntitySearchUp);
        p.add(butEntitySearchDown);
        p.add(comboQuick);
        p.add(butQuickSearchUp);
        p.add(butQuickSearchDown);
        p.add(butQuickFilter);
        p.add(butSwitchLocation);

        JScrollPane sp = new JScrollPane(p);
        JPanel panelMain = new JPanel(new BorderLayout());

        tabs = new JTabbedPane();
        panelMain.add(tabs, BorderLayout.CENTER);
        panelMain.add(sp, BorderLayout.SOUTH);
        panelMain.setMinimumSize(new Dimension(0, 0));
        add(panelMain, BorderLayout.CENTER);

        doLayout();
    }

    private void filterReport(String selectedKeyword) {
        String filterResult = "";
        String[] keywords = selectedKeyword.split(" ");
        String[] htmlLines = currentClient.phaseReport.split("<br>");
        for (int i = 0; i < htmlLines.length; i++) {
            String htmlLine = htmlLines[i];
            for (int j = 0; j < keywords.length; j++) {
                String word = keywords[j];
                if (htmlLine.replaceAll("<[^>]*>", "").toUpperCase().contains(word.toUpperCase())) {
                    if (i > 0 && htmlLines[i - 1].contains("<img")) {
                        filterResult += htmlLines[i - 1] + "<br>"; // get image from line above
                    }
                    filterResult += htmlLine + "<br>";
                    if (i < htmlLines.length - 1 && htmlLines[i + 1].contains("</div>")) {
                        filterResult += "</div>"; // close div tag
                    }
                    break;
                }
            }
        }

        filterReportOutput(filterResult);

        butQuickFilter.setText("Filter*");
        filterEnabled = true;

    }

    private void filterReportOutput(String text) {
        if (tabs.getTabCount() > 0) {
            int phaseTab = tabs.getTabCount() - 1;
            tabs.removeTabAt(phaseTab);
            tabs.add(Messages.getString("MiniReportDisplay.Phase"), loadHtmlScrollPane(text));
            tabs.setSelectedIndex(phaseTab);
        }
    }

    private void filterButtonReset() {
        butQuickFilter.setText("Filter");
        filterEnabled = false;
    }

    private void searchTextPane(String searchPattern, Boolean searchDown) {
        Component selCom = tabs.getSelectedComponent();
        searchPattern = searchPattern.toUpperCase();

        if (selCom instanceof JScrollPane && ((JScrollPane) selCom).getViewport().getView() instanceof JComponent) {
            JViewport v = ((JScrollPane) selCom).getViewport();
            for (Component comp : v.getComponents()) {
                if (comp instanceof JTextPane) {
                    try {
                        JTextPane textPane = (JTextPane) comp;
                        Document doc = textPane.getDocument();
                        String text = doc.getText(0, doc.getLength()).toUpperCase();
                        int currentPos = textPane.getCaretPosition();

                        if (currentPos > text.length() - searchPattern.length()) {
                            textPane.setCaretPosition(0);
                            currentPos = 0;
                        }

                        int newPos = -1;

                        if (searchDown) {
                            newPos = text.indexOf(searchPattern, currentPos);

                            if (newPos == -1) {
                                newPos = text.indexOf(searchPattern, 0);
                            }

                        } else {
                            newPos = text.lastIndexOf(searchPattern, currentPos - searchPattern.length() - 1);

                            if (newPos == -1) {
                                newPos = text.lastIndexOf(searchPattern, text.length() - searchPattern.length() - 1);
                            }
                        }

                        if (newPos != -1) {
                            Rectangle2D r = textPane.modelToView2D(newPos);
                            int y = UIUtil.calculateCenter(v.getExtentSize().height,
                                  v.getViewSize().height,
                                  (int) r.getHeight(),
                                  (int) r.getY());
                            v.setViewPosition(new Point(0, y));
                            textPane.setCaretPosition(newPos);
                            textPane.moveCaretPosition(newPos + searchPattern.length());
                            textPane.getCaret().setSelectionVisible(true);
                        }
                    } catch (Exception e) {
                    }

                    break;
                }
            }
        }
    }

    private void updatePlayerChoice() {
        String name = String.format("%-12s", currentClient.getName());
        String lastChoice = (String) comboPlayer.getSelectedItem();
        lastChoice = (lastChoice != null ? lastChoice : name);
        comboPlayer.removeAllItems();
        comboPlayer.setEnabled(true);
        List<Player> sortedPlayerList = currentClient.getGame().getPlayersList();
        sortedPlayerList.sort(Comparator.comparingInt(Player::getId));
        for (Player player : sortedPlayerList) {
            String playerDisplay = String.format("%-12s", player.getName());
            comboPlayer.addItem(playerDisplay);
        }
        comboPlayer.setSelectedItem(lastChoice);
        if (comboPlayer.getItemCount() <= 1) {
            comboPlayer.setEnabled(false);
        } else if (comboPlayer.getSelectedIndex() < 0) {
            comboPlayer.setSelectedIndex(0);
        }
    }

    private String addEntity(JComboBox comboBox, String name) {
        boolean found = false;
        int len = (name.length() < MRD_MAXNAMELENGHT ? name.length() : MRD_MAXNAMELENGHT);
        String displayNane = String.format("%-12s", name).substring(0, len);
        found = false;
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(displayNane)) {
                found = true;
                break;
            }
        }
        if (!found) {
            comboBox.addItem(displayNane);
        }
        return displayNane;
    }

    private void updateEntityChoice() {
        String lastChoice = (String) comboEntity.getSelectedItem();
        comboEntity.removeAllItems();
        comboEntity.setEnabled(true);
        String displayNane = "";
        for (Entity entity : currentClient.getGame().inGameTWEntities()) {
            if (entity.getOwner().equals(currentClient.getLocalPlayer())) {
                displayNane = addEntity(comboEntity, entity.getShortName());
            }
        }
        lastChoice = (lastChoice != null ? lastChoice : displayNane);
        comboEntity.setSelectedItem(lastChoice);
        if (comboEntity.getItemCount() <= 1) {
            comboEntity.setEnabled(false);
        } else if (comboEntity.getSelectedIndex() < 0) {
            comboEntity.setSelectedIndex(0);
        }
    }

    private void updateQuickChoice() {
        String lastChoice = (String) comboQuick.getSelectedItem();
        lastChoice = (lastChoice != null) ? lastChoice : Messages.getString("MiniReportDisplay.Damage");
        comboQuick.removeAllItems();
        comboQuick.setEnabled(true);
        String[] keywords = CP.getReportKeywords().split("\n");
        for (String keyword : keywords) {
            comboQuick.addItem(keyword);
        }
        comboQuick.setSelectedItem(lastChoice);
        if (comboQuick.getItemCount() <= 1) {
            comboQuick.setEnabled(false);
        } else if (comboQuick.getSelectedIndex() < 0) {
            comboQuick.setSelectedIndex(0);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            updatePlayerChoice();
            updateEntityChoice();
            updateQuickChoice();
        }
        super.setVisible(visible);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(butSwitchLocation)) {
            GUIP.toggleMiniReportLocation();
        } else if (ae.getSource().equals(butPlayerSearchDown)) {
            searchPattern(comboPlayer, true);
        } else if (ae.getSource().equals(butPlayerSearchUp)) {
            searchPattern(comboPlayer, false);
        } else if (ae.getSource().equals(butEntitySearchDown)) {
            searchPattern(comboEntity, true);
        } else if (ae.getSource().equals(butEntitySearchUp)) {
            searchPattern(comboEntity, false);
        } else if (ae.getSource().equals(butQuickSearchDown)) {
            searchPattern(comboQuick, true);
        } else if (ae.getSource().equals(butQuickSearchUp)) {
            searchPattern(comboQuick, false);
        } else if (ae.getSource().equals(butQuickFilter)) {
            if (!filterEnabled) {
                filterReport(comboQuick.getItemAt(comboQuick.getSelectedIndex()));
            } else {
                filterReportOutput(currentClient.phaseReport);
                filterButtonReset();
            }

        } else if (ae.getSource().equals(comboQuick)) {
            filterButtonReset();
        }
    }

    private void searchPattern(JComboBox<String> combo, boolean searchDown) {
        if (combo.getSelectedItem() == null) {
            return;
        }

        String searchPattern = combo.getSelectedItem().toString().trim();
        searchTextPane(searchPattern, searchDown);
    }

    private JScrollPane loadHtmlScrollPane(String t) {

        JTextPane ta = new JTextPane();
        Report.setupStylesheet(ta);
        ta.addHyperlinkListener(this);
        BASE64ToolKit toolKit = new BASE64ToolKit();
        ta.setEditorKit(toolKit);

        ta.setText("<div class='report'>" + t + "</div>");

        ta.setEditable(false);
        ta.setOpaque(false);
        ta.setCaretPosition(0);
        filterButtonReset();
        return new JScrollPane(ta);
    }

    public void addReportPages(GamePhase phase) {
        int numRounds = currentClient.getGame().getRoundCount();
        int startIndex = 1;

        // only reload what has changed
        if (numRounds < 2 || phase.isVictory()) {
            tabs.removeAll();
        } else if (tabs.getTabCount() > 1) {
            tabs.removeTabAt(tabs.getTabCount() - 1);
            // don't remove on round change
            if (tabs.getTabCount() == numRounds) {
                tabs.removeTabAt(tabs.getTabCount() - 1);
            }
            startIndex = tabs.getTabCount() + 1;
        }

        for (int round = startIndex; round <= numRounds; round++) {
            String text = currentClient.receiveReport(currentClient.getGame().getReports(round));
            tabs.add(Messages.getString("MiniReportDisplay.Round") + " " + round, loadHtmlScrollPane(text));
        }

        // add the new current phase tab
        tabs.add(Messages.getString("MiniReportDisplay.Phase"), loadHtmlScrollPane(currentClient.phaseReport));

        tabs.setSelectedIndex(tabs.getTabCount() - 1);
        tabs.setMinimumSize(new Dimension(0, 0));
    }

    private JComponent activePane() {
        return (JComponent) ((JScrollPane) tabs.getSelectedComponent()).getViewport().getView();
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent evt) {
        String evtDesc = evt.getDescription();
        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (evtDesc.startsWith(Report.ENTITY_LINK)) {
                String idString = evtDesc.substring(Report.ENTITY_LINK.length());
                int id;
                try {
                    id = Integer.parseInt(idString);
                } catch (Exception ex) {
                    id = -1;
                }
                var optionalEntity = currentClientgui.getClient().getGame().getInGameObject(id);
                if (optionalEntity.isPresent() && optionalEntity.get() instanceof Entity entity) {
                    if (currentClientgui instanceof IHasUnitDisplay) {
                        ((IHasUnitDisplay) currentClientgui).getUnitDisplay().displayEntity(entity);
                        GUIP.setUnitDisplayEnabled(true);
                        if (entity.isDeployed() && !entity.isOffBoard() && entity.getPosition() != null) {
                            if (currentClientgui instanceof IHasBoardView) {
                                ((IHasBoardView) currentClientgui).getBoardView().centerOnHex(entity.getPosition());
                            }
                        }
                    }

                }
            } else if (evtDesc.startsWith(Report.TOOLTIP_LINK)) {
                String desc = evtDesc.substring(Report.TOOLTIP_LINK.length());
                JOptionPane.showMessageDialog(currentClientgui.getFrame(),
                      desc,
                      Messages.getString("MiniReportDisplay.Details"),
                      JOptionPane.PLAIN_MESSAGE);
            }
        } else if (evt.getEventType() == HyperlinkEvent.EventType.ENTERED) {
            if (evtDesc.startsWith(Report.TOOLTIP_LINK)) {
                String desc = evtDesc.substring(Report.TOOLTIP_LINK.length());
                activePane().setToolTipText(desc);
            }
        } else if (evt.getEventType() == HyperlinkEvent.EventType.EXITED) {
            activePane().setToolTipText(null);
        }
    }

    private GameListener gameListener = new GameListenerAdapter() {
        @Override
        public void gamePhaseChange(GamePhaseChangeEvent e) {
            switch (e.getOldPhase()) {
                case VICTORY:
                    setVisible(false);
                    break;
                default:
                    if ((!e.getNewPhase().equals((e.getOldPhase()))) && ((e.getNewPhase().isReport())
                          || ((e.getNewPhase().isOnMap()) && (tabs.getTabCount() == 0)))) {
                        addReportPages(e.getNewPhase());
                        updatePlayerChoice();
                        updateEntityChoice();
                    }
            }
        }
    };
}
