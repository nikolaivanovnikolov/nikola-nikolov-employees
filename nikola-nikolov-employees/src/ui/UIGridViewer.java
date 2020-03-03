package ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import app.LongestProjectCoworkers;
import data.CommonActivity;
import data.EmployeeActivity;
import ui.swing.DataProvider;
import ui.swing.ObjectTableModel;
import ui.swing.TableDecorator;

public class UIGridViewer {

    public static void createUIGrid(List<CommonActivity> maxCommonActivities) {
        final JFrame frame = createFrame();
        JTable table = new JTable(createObjectDataModel());
        table.setAutoCreateRowSorter(true);

        JPanel contentPanel = new JPanel(new GridLayout(2,1));

        DataProvider<CommonActivity> dataProvider = createDataProvider(maxCommonActivities);
        TableDecorator<CommonActivity> pagedDecorator = TableDecorator.decorate(table, dataProvider, new int[]{5, 10, 20, 50, 75, 100}, 10);
        JPanel gridPanel = pagedDecorator.getGridPanel();
        contentPanel.add(gridPanel);

        JButton loadButton = new JButton("Load File");
        loadButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                File file = chooseFile(new JLabel("Load File"), frame);
                final List<EmployeeActivity> activities = LongestProjectCoworkers.loadDataFromFile(file);
                DataProvider<CommonActivity> dataProvider = createDataProvider(LongestProjectCoworkers.findMaxCommonActivities(activities));
                pagedDecorator.setDataProvider(dataProvider);
                pagedDecorator.repaintTable();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        contentPanel.add(buttonPanel);

        frame.add(contentPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static TableModel createObjectDataModel() {
        return new ObjectTableModel<CommonActivity>() {
            @Override
            public Object getValueAt(CommonActivity commonActivity, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return commonActivity.getProjectID();
                    case 1:
                        return commonActivity.getEmployeeID1();
                    case 2:
                        return commonActivity.getEmployeeID2();
                    case 3:
                        return commonActivity.getWorkedDays();
                }
                return null;
            }

            @Override
            public int getColumnCount() {
                return 4;
            }
            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Project ID";
                    case 1:
                        return "Employee1 ID";
                    case 2:
                        return "Employee2 ID";
                    case 3:
                        return "Common work days";
                }
                return null;
            }
        };
    }

    private static DataProvider<CommonActivity> createDataProvider(final List<CommonActivity> list) {

        return new DataProvider<CommonActivity>() {
            @Override
            public int getTotalRowCount() {
                return list.size();
            }

            @Override
            public List<CommonActivity> getRows(int startIndex, int endIndex) {
                return list.subList(startIndex, endIndex);
            }
        };
    }

        private static JFrame createFrame() {
        JFrame frame = new JFrame("Paged JTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        return frame;
    }

    private static File chooseFile(JLabel fileLabel, JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        String location = AppPrefs.FileLocation.get(System.getProperty("user.home"));
        fileChooser.setCurrentDirectory(new File(location));

        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileLabel.setText(selectedFile.getAbsolutePath());
            AppPrefs.FileLocation.put(selectedFile.getParentFile().getAbsolutePath());
        }
        
        return fileChooser.getSelectedFile();
    }

    enum AppPrefs {
        FileLocation;
        private static Preferences prefs = Preferences.userRoot()
                                              .node(AppPrefs.class.getName());

        String get(String defaultValue) {
            return prefs.get(this.name(), defaultValue);
        }

        void put(String value) {
            prefs.put(this.name(), value);
        }
    }
}