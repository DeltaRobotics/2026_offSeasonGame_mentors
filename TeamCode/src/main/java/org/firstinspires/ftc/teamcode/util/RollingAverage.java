package org.firstinspires.ftc.teamcode.util;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

public class RollingAverage
{
    private int n;
    private Deque<Double> deque;
    private double[] weights;

    public RollingAverage(int n)
    {
        this.n = n;
        deque = new LinkedList<Double>();
        // Initialize linked list with all zeros.
        for(int i = 0; i < n; ++i)
        {
            deque.add(0.0);
        }

        weights = new double[n];
        // Default weights to all equal values.
        final double WEIGHT_VALUE = 1.0 / n;
        for(int i = 0; i < n; ++i)
        {
            weights[i] = WEIGHT_VALUE;
        }
    }

    public RollingAverage(int n, double[] weights)
    {
        this(n);
        System.arraycopy(weights, 0, this.weights, 0, n);
    }

    public RollingAverage(int n, double deltaT, Collection<Double> weights)
    {
        this(n);
        System.arraycopy(weights.toArray(new Double[0]), 0, this.weights, 0, n);
    }

    public double GetAverage()
    {
        double result = 0;
        int index = 0;
        for (Double val : deque)
        {
            result += weights[index] * val;
            index++;
        }

        return result / n;
    }

    public double Update(double reading)
    {
        deque.removeLast();
        deque.push(reading);

        return GetAverage();
    }
}
