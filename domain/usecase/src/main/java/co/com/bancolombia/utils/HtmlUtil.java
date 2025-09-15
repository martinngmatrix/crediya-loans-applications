package co.com.bancolombia.utils;

import java.math.BigInteger;

import co.com.bancolombia.model.debtcapacity.DebtCapacity;

public class HtmlUtil {
    public static String generatePaymentPlanHtml(DebtCapacity debtCapacity) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><body>");
        sb.append("<h2>Plan de Pago - Solicitud #").append(debtCapacity.getLoanApplicationId()).append("</h2>");
        sb.append("<p>Estado: ").append(debtCapacity.getResult()).append("</p>");
        sb.append("<p>Cuota mensual: ").append(String.format("%.2f", debtCapacity.getNewLoanInstallment())).append("</p>");

        sb.append("<table border='1' cellpadding='5' cellspacing='0'>");
        sb.append("<tr>")
        .append("<th>Mes</th>")
        .append("<th>Cuota</th>")
        .append("<th>Capital</th>")
        .append("<th>Interés</th>")
        .append("<th>Saldo</th>")
        .append("</tr>");

        if (debtCapacity.getPaymentPlan() != null) {
            debtCapacity.getPaymentPlan().forEach(entry -> {
                sb.append("<tr>")
                .append("<td>").append(entry.getMonth()).append("</td>")
                .append("<td>").append(String.format("%.2f", entry.getInstallment())).append("</td>")
                .append("<td>").append(String.format("%.2f", entry.getCapital())).append("</td>")
                .append("<td>").append(String.format("%.2f", entry.getInterest())).append("</td>")
                .append("<td>").append(String.format("%.2f", entry.getRemainingBalance())).append("</td>")
                .append("</tr>");
            });
        }

        sb.append("</table>");
        sb.append("</body></html>");

        return sb.toString();
    }

    public static String generateStatusHtml(BigInteger loanApplicationId, String status) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><body style='font-family: Arial, sans-serif;'>");
        sb.append("<h2 style='color:#2E86C1;'>Estado de su Solicitud</h2>");
        sb.append("<p>Estimado cliente,</p>");
        sb.append("<p>La solicitud con número <b>")
          .append(loanApplicationId)
          .append("</b> tiene el siguiente estado:</p>");

        sb.append("<p style='font-size:16px; color:#117A65;'><b>")
          .append(status)
          .append("</b></p>");

        sb.append("<br><p>Gracias por confiar en nosotros.</p>");
        sb.append("</body></html>");

        return sb.toString();
    }
}
