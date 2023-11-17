package splitter.service;

import org.springframework.stereotype.Service;
import splitter.entity.Transaction;
import splitter.entity.User;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BalanceReducer {
    private Map<User, List<Transaction>> graph;
    private List<Transaction> order;
    private Set<User> visited;
    public List<Transaction> simplifyRepayments(List<Transaction> input) {
        toGraph(input);
        dfs();
        for (Transaction t : input) {
            if (t.amount.signum() == -1) {
                User tmp = t.to;
                t.to = t.from;
                t.from = tmp;
                t.amount = t.amount.negate();
            }
        }
        return input;
    }

    private void toGraph(List<Transaction> input) {
        graph = new HashMap<>();
        for (Transaction transaction : input) {
            graph.putIfAbsent(transaction.from, new ArrayList<>());
            graph.putIfAbsent(transaction.to, new ArrayList<>());

            graph.get(transaction.from).add(transaction);
            graph.get(transaction.to).add(transaction);
        }
    }

    private void dfs() {
        visited = new HashSet<>();
        for (User user : graph.keySet()) {
            order = new ArrayList<>();
            visit(user, null);
        }
    }

    private void visit(User node, User parent) {
        if (visited.contains(node)) {
            reduce(node);
            return;
        }
        visited.add(node);

        for (Transaction edge : graph.get(node)) {
            if (edge.amount.signum() == 0) continue;

            User anotherNode = getAnotherNode(edge, node);
            if (anotherNode.equals(parent)) continue;

            order.add(edge);
            visit(anotherNode, node);
            order.remove(order.size() - 1);
        }
    }

    private User getAnotherNode(Transaction edge, User node) {
        return edge.from.equals(node) ? edge.to : edge.from;
    }

    private void reduce(User node) {
        if (order.isEmpty()) return;
        Transaction edge = order.get(order.size() - 1);
        BigDecimal amount = edge.from.equals(node) ? edge.amount : edge.amount.negate();

        User cycleNode = node;

        for (int i = order.size() - 1; i >= 0; i--) {
            Transaction cycleEdge = order.get(i);
            cycleNode = getAnotherNode(cycleEdge, cycleNode);
            cycleEdge.amount = cycleEdge.from.equals(cycleNode)
                    ? cycleEdge.amount.add(amount)
                    : cycleEdge.amount.subtract(amount);
            if (cycleNode.equals(node)) break;
        }
    }
}
