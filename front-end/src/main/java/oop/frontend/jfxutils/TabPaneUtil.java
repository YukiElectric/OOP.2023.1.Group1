package oop.frontend.jfxutils;

import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import oop.frontend.common.Constants;
import oop.frontend.controller.PostItemController;
/**
 * Các phương thức tiện ích để quản lý TabPane trong ứng dụng JavaFX.
 */
public class TabPaneUtil {

    /**
     * Thiết lập chức năng cho TabPane khi chọn các mục trong ToggleGroup.
     *
     * @param menu    ToggleGroup chứa các nút để chọn Tab.
     * @param tabPane TabPane cần được điều khiển.
     * @param index   Chỉ số ban đầu của TabPane.
     */    private static void setTabPane(ToggleGroup menu, TabPane tabPane, int index) {
        menu.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
        menu.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (menu.getSelectedToggle() != null) {
                ToggleButton selectedButton = (ToggleButton) menu.getSelectedToggle();
                int selectedIndex = menu.getToggles().indexOf(selectedButton);
                tabPane.getSelectionModel().select(selectedIndex + index);
            }
        });
    }

    public static void setSelection(ToggleGroup menu, TabPane tabPane) {
        menu.getToggles().get(0).setSelected(true);
        setTabPane(menu, tabPane, 0);
    }

    public static void setSelection(ToggleGroup menu, TabPane tabPane, int index) {
        setTabPane(menu, tabPane, index);
    }

    public static void resetIndexTabPane(ToggleGroup menu, ToggleGroup topMenu, ToggleGroup trendingMenu) {
        menu.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                topMenu.getToggles().get(0).setSelected(true);
                trendingMenu.getToggles().get(0).setSelected(true);
            }
        }));
    }

    public static void setViewChange(ToggleGroup toggleGroup, VBox postView, VBox hotTagView, VBox trendingView, VBox topView, int index) {
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                ToggleButton selectedButton = (ToggleButton) toggleGroup.getSelectedToggle();
                int selectedIndex = toggleGroup.getToggles().indexOf(selectedButton);
                if (index == 0)
                    switch (selectedIndex) {
                        case 0 -> VboxViewUtil.setViewVBox(postView, Constants.API_URL, "", PostItemController.class);
                        case 1 -> VboxViewUtil.setViewVBox(hotTagView, Constants.API_URL, "", PostItemController.class);
                        case 2 -> VboxViewUtil.setViewVBox(trendingView, Constants.API_URL, "", PostItemController.class);
                        case 3 -> VboxViewUtil.setViewVBox(topView, Constants.API_URL, "", PostItemController.class);
                        default -> {
                        }
                    }
                else
                    switch (selectedIndex) {
                        case 0 -> VboxViewUtil.setViewVBox(postView, Constants.API_URL, "", PostItemController.class);
                        case 1 -> VboxViewUtil.setViewVBox(hotTagView, Constants.API_URL, "", PostItemController.class);
                        case 2 -> VboxViewUtil.setViewVBox(trendingView, Constants.API_URL, "", PostItemController.class);
                        case 3 -> VboxViewUtil.setViewVBox(topView, Constants.API_URL, "", PostItemController.class);
                        default -> {
                        }
                    }
            }
        });
    }
}
