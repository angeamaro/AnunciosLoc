package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import ao.co.isptec.aplm.anunciosloc.R;
import ao.co.isptec.aplm.anunciosloc.data.model.Announcement;
import ao.co.isptec.aplm.anunciosloc.data.repository.UserRepository;
import ao.co.isptec.aplm.anunciosloc.ui.viewmodel.AnnouncementViewModel;
import ao.co.isptec.aplm.anunciosloc.utils.DateUtils;

/**
 * Activity para exibir detalhes de um anúncio
 */
public class AnnouncementDetailActivity extends AppCompatActivity {
    
    public static final String EXTRA_ANNOUNCEMENT_ID = "announcement_id";
    
    private MaterialToolbar toolbar;
    private TextView txtTitle, txtContent, txtLocation, txtAuthor, txtCreatedDate, txtStartDate, txtEndDate, txtViewCount;
    private TextView txtTimeRemaining, txtStatus, txtPolicy;
    private Chip chipActive;
    private ImageView imgLocation;
    private MaterialButton btnEdit, btnDelete;
    private ProgressBar progressBar;
    private View layoutActions;
    
    private AnnouncementViewModel viewModel;
    private UserRepository userRepository;
    
    private String announcementId;
    private Announcement currentAnnouncement;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_detail);
        
        announcementId = getIntent().getStringExtra(EXTRA_ANNOUNCEMENT_ID);
        
        if (announcementId == null) {
            Toast.makeText(this, "Erro: Anúncio não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initializeViews();
        initializeViewModel();
        setupToolbar();
        setupListeners();
        observeViewModel();
        loadAnnouncementDetails();
    }
    
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);
        txtLocation = findViewById(R.id.txtLocation);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtCreatedDate = findViewById(R.id.txtCreatedDate);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        txtViewCount = findViewById(R.id.txtViewCount);
        txtTimeRemaining = findViewById(R.id.txtTimeRemaining);
        txtStatus = findViewById(R.id.txtStatus);
        txtPolicy = findViewById(R.id.txtPolicy);
        chipActive = findViewById(R.id.chipActive);
        imgLocation = findViewById(R.id.imgLocation);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        progressBar = findViewById(R.id.progressBar);
        layoutActions = findViewById(R.id.layoutActions);
    }
    
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);
        userRepository = UserRepository.getInstance();
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalhes do Anúncio");
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupListeners() {
        btnEdit.setOnClickListener(v -> {
            // TODO: Abrir CreateAnnouncementActivity em modo edição
            Toast.makeText(this, "Editar anúncio", Toast.LENGTH_SHORT).show();
        });
        
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Eliminar Anúncio")
                .setMessage("Tem certeza que deseja eliminar este anúncio? Esta ação não pode ser desfeita.")
                .setPositiveButton(R.string.yes, (dialog, which) -> deleteAnnouncement())
                .setNegativeButton(R.string.no, null)
                .show();
        });
        
        imgLocation.setOnClickListener(v -> {
            if (currentAnnouncement != null) {
                Toast.makeText(this, "Ver local: " + currentAnnouncement.getLocationName(), Toast.LENGTH_SHORT).show();
                // TODO: Abrir mapa ou detalhes da localização
            }
        });
    }
    
    private void observeViewModel() {
        // Observa lista de anúncios
        viewModel.getAnnouncements().observe(this, announcements -> {
            if (announcements != null) {
                for (Announcement announcement : announcements) {
                    if (announcement.getId().equals(announcementId)) {
                        currentAnnouncement = announcement;
                        displayAnnouncementDetails(announcement);
                        break;
                    }
                }
            }
        });
        
        // Observa estado de carregamento
        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
        
        // Observa mensagens de erro
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
        
        // Observa sucesso de eliminação
        viewModel.getAnnouncementDeleted().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, R.string.success_announcement_deleted, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    
    private void loadAnnouncementDetails() {
        viewModel.loadAnnouncements();
    }
    
    private void displayAnnouncementDetails(Announcement announcement) {
        // Título e conteúdo
        txtTitle.setText(announcement.getTitle());
        txtContent.setText(announcement.getContent());
        
        // Localização
        txtLocation.setText(announcement.getLocationName());
        
        // Autor
        txtAuthor.setText("Por: " + announcement.getAuthorName());
        
        // Datas
        txtCreatedDate.setText("Publicado: " + DateUtils.formatDateTime(announcement.getCreatedAt()));
        txtStartDate.setText("Início: " + DateUtils.formatDateTime(announcement.getStartDate()));
        txtEndDate.setText("Término: " + DateUtils.formatDateTime(announcement.getEndDate()));
        
        // Visualizações
        txtViewCount.setText(announcement.getViewCount() + " visualizações");
        
        // Tempo restante
        if (announcement.getEndDate() != null && announcement.getEndDate().getTime() > System.currentTimeMillis()) {
            String remaining = DateUtils.getTimeRemaining(announcement.getEndDate());
            txtTimeRemaining.setText(remaining);
            txtTimeRemaining.setVisibility(View.VISIBLE);
        } else {
            txtTimeRemaining.setVisibility(View.GONE);
        }
        
        // Status
        if (announcement.isActive()) {
            chipActive.setText("Ativo");
            chipActive.setChipBackgroundColorResource(R.color.success);
            txtStatus.setText("Este anúncio está ativo e sendo distribuído");
        } else {
            chipActive.setText("Inativo");
            chipActive.setChipBackgroundColorResource(R.color.gray_400);
            txtStatus.setText("Este anúncio não está mais ativo");
        }
        
        // Política de entrega
        String policyText;
        switch (announcement.getDeliveryPolicy()) {
            case "EVERYONE":
                policyText = "Todos os usuários";
                break;
            case "WHITELIST":
                policyText = "Apenas usuários específicos (Whitelist)";
                break;
            case "BLACKLIST":
                policyText = "Excluindo usuários específicos (Blacklist)";
                break;
            default:
                policyText = announcement.getDeliveryPolicy();
        }
        txtPolicy.setText("Política: " + policyText);
        
        // Verifica se usuário atual é o autor
        if (userRepository.getCurrentUser() != null && 
            announcement.getAuthorId().equals(userRepository.getCurrentUser().getId())) {
            layoutActions.setVisibility(View.VISIBLE);
        } else {
            layoutActions.setVisibility(View.GONE);
        }
    }
    
    private void deleteAnnouncement() {
        if (announcementId != null) {
            viewModel.deleteAnnouncement(announcementId);
        }
    }
}
