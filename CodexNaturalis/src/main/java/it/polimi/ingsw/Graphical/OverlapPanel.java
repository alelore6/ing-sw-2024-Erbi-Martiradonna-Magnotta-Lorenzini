package it.polimi.ingsw.Graphical;

import javax.swing.*;
import java.awt.*;

class OverlapPanel extends JPanel {
    private int gridSize;
    private int overlapOffset;

    public OverlapPanel(int gridSize, int overlapOffset) {
        this.gridSize = gridSize;
        this.overlapOffset = overlapOffset;
        setLayout(null); // Layout manager nullo per posizionamento assoluto
    }

    @Override
    public void doLayout() {
        int numComponents = getComponentCount();
        int rowCount = (int) Math.sqrt(numComponents);
        int colCount = rowCount;

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                int index = i * colCount + j;
                if (index < numComponents) {
                    Component comp = getComponent(index);
                    int x = j * (comp.getPreferredSize().width - overlapOffset);
                    int y = i * (comp.getPreferredSize().height - overlapOffset);
                    comp.setBounds(x, y, comp.getPreferredSize().width, comp.getPreferredSize().height);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (getComponentCount() > 0) {
            int rowCount = gridSize;
            int colCount = gridSize;
            int componentWidth = getComponent(0).getPreferredSize().width;
            int componentHeight = getComponent(0).getPreferredSize().height;
            int preferredWidth = (colCount - 1) * (componentWidth - overlapOffset) + componentWidth;
            int preferredHeight = (rowCount - 1) * (componentHeight - overlapOffset) + componentHeight;
            return new Dimension(preferredWidth, preferredHeight);
        } else {
            return super.getPreferredSize();
        }
    }
}

