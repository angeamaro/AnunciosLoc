package ao.co.isptec.aplm.anunciosloc.data;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.InterestCategory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterestsData {

    public static List<InterestCategory> getPredefinedInterests() {
        List<InterestCategory> categories = new ArrayList<>();

        categories.add(new InterestCategory(
                "alimentacao",
                "Alimentação",
                R.drawable.ic_restaurant,
                Arrays.asList(
                        "Restaurantes",
                        "Cafés",
                        "Padarias",
                        "Fast Food",
                        "Comida Saudável",
                        "Docerias",
                        "Pizzarias",
                        "Churrascarias"
                )
        ));

        categories.add(new InterestCategory(
                "tecnologia",
                "Tecnologia",
                R.drawable.ic_computer,
                Arrays.asList(
                        "Smartphones",
                        "Computadores",
                        "Tablets",
                        "Gadgets",
                        "Software",
                        "Gaming",
                        "Inteligência Artificial",
                        "Realidade Virtual"
                )
        ));

        categories.add(new InterestCategory(
                "esportes",
                "Esportes",
                R.drawable.ic_sports,
                Arrays.asList(
                        "Futebol",
                        "Basquete",
                        "Vôlei",
                        "Natação",
                        "Corrida",
                        "Academia",
                        "Ciclismo",
                        "Artes Marciais"
                )
        ));

        categories.add(new InterestCategory(
                "entretenimento",
                "Entretenimento",
                R.drawable.ic_movie,
                Arrays.asList(
                        "Cinema",
                        "Teatro",
                        "Shows",
                        "Festivais",
                        "Museus",
                        "Parques Temáticos",
                        "Eventos Culturais",
                        "Stand-up Comedy"
                )
        ));

        categories.add(new InterestCategory(
                "educacao",
                "Educação",
                R.drawable.ic_school,
                Arrays.asList(
                        "Cursos Online",
                        "Idiomas",
                        "Universidades",
                        "Bibliotecas",
                        "Workshops",
                        "Palestras",
                        "Tutoriais",
                        "Certificações"
                )
        ));

        categories.add(new InterestCategory(
                "saude",
                "Saúde e Bem-estar",
                R.drawable.ic_health,
                Arrays.asList(
                        "Médicos",
                        "Dentistas",
                        "Farmácias",
                        "Clínicas",
                        "Yoga",
                        "Meditação",
                        "Spa",
                        "Nutrição"
                )
        ));

        categories.add(new InterestCategory(
                "viagens",
                "Viagens",
                R.drawable.ic_travel,
                Arrays.asList(
                        "Hotéis",
                        "Pousadas",
                        "Turismo",
                        "Praias",
                        "Montanhas",
                        "Cidades Históricas",
                        "Aventuras",
                        "Cruzeiros"
                )
        ));

        categories.add(new InterestCategory(
                "compras",
                "Compras",
                R.drawable.ic_shopping,
                Arrays.asList(
                        "Shopping Centers",
                        "Roupas",
                        "Calçados",
                        "Acessórios",
                        "Eletrônicos",
                        "Livros",
                        "Decoração",
                        "Cosméticos"
                )
        ));

        categories.add(new InterestCategory(
                "servicos",
                "Serviços",
                R.drawable.ic_services,
                Arrays.asList(
                        "Salão de Beleza",
                        "Barbearia",
                        "Oficina Mecânica",
                        "Lavanderia",
                        "Pet Shop",
                        "Limpeza",
                        "Manutenção",
                        "Consultoria"
                )
        ));

        categories.add(new InterestCategory(
                "artes",
                "Artes e Cultura",
                R.drawable.ic_art,
                Arrays.asList(
                        "Pintura",
                        "Escultura",
                        "Fotografia",
                        "Música",
                        "Dança",
                        "Literatura",
                        "Artesanato",
                        "Design"
                )
        ));

        return categories;
    }
}
