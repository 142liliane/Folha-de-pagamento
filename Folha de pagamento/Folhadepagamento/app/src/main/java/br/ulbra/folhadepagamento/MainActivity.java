package br.ulbra.folhadepagamento;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNome, editTextSalarioBruto, editTextFilhos;
    private RadioButton radioButtonMasculino, radioButtonFeminino;
    private TextView textViewInss, textViewIr, textViewSalarioLiquido;
    private Button buttonCalcular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNome = findViewById(R.id.editTextNome);
        editTextSalarioBruto = findViewById(R.id.editTextSalarioBruto);
        editTextFilhos = findViewById(R.id.editTextFilhos);
        radioButtonMasculino = findViewById(R.id.radioButtonMasculino);
        radioButtonFeminino = findViewById(R.id.radioButtonFeminino);
        textViewInss = findViewById(R.id.textViewInss);
        textViewIr = findViewById(R.id.textViewIr);
        textViewSalarioLiquido = findViewById(R.id.textViewSalarioLiquido);
        buttonCalcular = findViewById(R.id.buttonCalcular);

        buttonCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularSalarioLiquido();
            }
        });
    }

    private void calcularSalarioLiquido() {
        String nome = editTextNome.getText().toString();
        String salarioBrutoString = editTextSalarioBruto.getText().toString();
        String filhosString = editTextFilhos.getText().toString();

        if (nome.isEmpty() || salarioBrutoString.isEmpty() || filhosString.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double salarioBruto = Double.parseDouble(salarioBrutoString);
        int numeroFilhos = Integer.parseInt(filhosString);

        if (salarioBruto < 0 || numeroFilhos < 0) {
            Toast.makeText(this, "Salário e número de filhos devem ser não negativos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double descontoINSS = calcularDescontoINSS(salarioBruto);
        double descontoIR = calcularDescontoIR(salarioBruto - descontoINSS);
        double salarioFamilia = calcularSalarioFamilia(salarioBruto, numeroFilhos);
        double salarioLiquido = salarioBruto - (descontoINSS + descontoIR) + salarioFamilia;

        String tratamento = radioButtonMasculino.isChecked() ? "Sr. " : "Sra. ";
        textViewInss.setText(String.format("INSS: R$ %.2f", descontoINSS));
        textViewIr.setText(String.format("IR: R$ %.2f", descontoIR));
        textViewSalarioLiquido.setText(String.format("Salário Líquido: R$ %.2f", salarioLiquido));
        Toast.makeText(this, "Cálculo realizado com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private double calcularDescontoINSS(double salario) {
        if (salario <= 1212.00) {
            return salario * 0.075;
        } else if (salario <= 2427.35) {
            return salario * 0.09;
        } else if (salario <= 3641.03) {
            return salario * 0.12;
        } else if (salario <= 7087.22) {
            return salario * 0.14;
        }
        return 0;
    }

    private double calcularDescontoIR(double salario) {
        if (salario <= 1903.98) {
            return 0;
        } else if (salario <= 2826.65) {
            return salario * 0.075;
        } else if (salario <= 3751.05) {
            return salario * 0.15;
        } else if (salario <= 4664.68) {
            return salario * 0.225;
        }
        return 0;
    }

    private double calcularSalarioFamilia(double salario, int filhos) {
        if (salario <= 1212.00) {
            return 56.47 * filhos;
        }
        return 0;
    }
}