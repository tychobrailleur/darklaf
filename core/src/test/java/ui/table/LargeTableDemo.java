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
 *
 */
package ui.table;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import ui.ComponentDemo;

public class LargeTableDemo implements ComponentDemo {

    private static final int COLUMN_COUNT = 100;
    private static final int CELL_COUNT = 5000000;

    public static void main(final String[] args) {
        ComponentDemo.showDemo(new LargeTableDemo());
    }

    @Override
    public JComponent createComponent() {
        JPanel holder = new JPanel();
        holder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTable table = new JTable();
        table.setModel(new DarkTableModel(CELL_COUNT / COLUMN_COUNT, COLUMN_COUNT));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        holder.add(new JScrollPane(table));
        return holder;
    }

    @Override
    public String getTitle() {
        return "Large Table Demo";
    }

    static class DarkTableModel extends AbstractTableModel {

        int rows;
        int cols;

        public DarkTableModel(final int rows, final int cols) {
            this.rows = rows;
            this.cols = cols;
        }

        @Override
        public int getRowCount() {
            return rows;
        }

        @Override
        public int getColumnCount() {
            return cols;
        }

        @Override
        public Object getValueAt(final int rowIndex, final int columnIndex) {
            return rowIndex * cols + columnIndex;
        }
    }
}
