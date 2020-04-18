/*
 * MIT License
 *
 * Copyright (c) 2020 Jannis Weis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ui;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.settings.ThemeSettings;
import com.github.weisj.darklaf.theme.Theme;
import com.github.weisj.darklaf.theme.info.ColorToneRule;
import com.github.weisj.darklaf.theme.info.ContrastRule;
import com.github.weisj.darklaf.theme.info.PreferredThemeStyle;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;

public interface ComponentDemo {

    static Theme getTheme() {
        return LafManager.themeForPreferredStyle(new PreferredThemeStyle(ContrastRule.STANDARD,
                                                                         ColorToneRule.LIGHT));
    }

    JComponent createComponent();

    static void showDemo(final ComponentDemo demo) {
        showDemo(demo, null);
    }

    static void showDemo(final ComponentDemo demo, final Dimension dimension) {
        SwingUtilities.invokeLater(() -> {
            LafManager.install(demo.createTheme());
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addWindowListener(demo.createWindowListener());
            frame.setTitle(demo.getTitle());
            frame.setContentPane(demo.createComponent());
            frame.setJMenuBar(demo.createMenuBar());
            Image image = demo.getIconImage();
            if (image != null) frame.setIconImage(image);

            frame.pack();
            if (dimension == null) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension dim = new Dimension(screenSize.width / 2,
                                              screenSize.height / 2);
                Dimension targetSize = frame.getSize();
                targetSize.width = Math.min(targetSize.width, dim.width);
                targetSize.height = Math.min(targetSize.height, dim.height);
                frame.setSize(targetSize);
            } else {
                frame.setSize(dimension);
            }
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }

    default Image getIconImage() {
        return null;
    }

    default WindowListener createWindowListener() {
        return new WindowAdapter() {
        };
    }

    static JMenu createThemeMenu() {
        JMenu menu = new JMenu("Theme");
        ButtonGroup bg = new ButtonGroup();
        for (Theme theme : LafManager.getRegisteredThemes()) {
            createThemeItem(menu, bg, theme);
        }
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(final MenuEvent e) {
                bg.setSelected(Optional.ofNullable(getSelectedThemeButton(bg))
                                       .map(AbstractButton::getModel).orElse(null), true);
            }

            @Override
            public void menuDeselected(final MenuEvent e) {

            }

            @Override
            public void menuCanceled(final MenuEvent e) {

            }
        });
        return menu;
    }

    static AbstractButton getSelectedThemeButton(final ButtonGroup bg) {
        String currentThemeName = LafManager.getTheme().getName();
        Enumeration<AbstractButton> enumeration = bg.getElements();
        while (enumeration.hasMoreElements()) {
            ThemeMenuItem mi = (ThemeMenuItem) enumeration.nextElement();
            if (Objects.equals(currentThemeName, mi.getName())) return mi;
        }
        return null;
    }

    static JMenuItem createSettingsMenu() {
        JMenu menu = new JMenu("Settings");
        JMenuItem mi = new JMenuItem("Theme Options");
        mi.addActionListener(e -> ThemeSettings.showSettingsDialog(menu));
        menu.add(mi);
        return menu;
    }

    static void createThemeItem(final JMenu menu, final ButtonGroup bg, final Theme theme) {
        final ThemeMenuItem mi = new ThemeMenuItem(theme);
        menu.add(mi);
        bg.add(mi);
    }

    default JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createThemeMenu());
        menuBar.add(createSettingsMenu());
        return menuBar;
    }

    default Theme createTheme() {
        return getTheme();
    }

    String getTitle();

    class ThemeMenuItem extends JRadioButtonMenuItem {

        private final String name;

        public ThemeMenuItem(final Theme theme) {
            name = theme.getName();
            final Action action = new AbstractAction(name) {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    Theme current = LafManager.getTheme();
                    LafManager.install(theme.derive(current.getFontSizeRule(), current.getAccentColorRule()));
                }
            };
            setAction(action);
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
