package ao.co.isptec.aplm.anunciosloc.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

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
    
    private ImageButton btnBack;
    private TextView txtTitle, txtContent, txtLocation, txtAuthor, txtCreatedDate, txtStartDate, txtEndDate;
    private TextView txtTimeRemaining, txtPolicy, badgeStatus;
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
        setupListeners();
        observeViewModel();
        loadAnnouncementDetails();
    }
    
    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);
        txtLocation = findViewById(R.id.txtLocation);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtCreatedDate = findViewById(R.id.txtCreatedDate);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        txtTimeRemaining = findViewById(R.id.txtTimeRemaining);
        txtPolicy = findViewById(R.id.txtPolicy);
        badgeStatus = findViewById(R.id.badgeStatus);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        progressBar = findViewById(R.id.progressBar);
        layoutActions = findViewById(R.id.layoutActions);
    }
    
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);
        userRepository = UserRepository.getInstance();
    }
    
    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        
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
        txtAuthor.setText(announcement.getAuthorName());
        
        // Datas
        txtCreatedDate.setText(DateUtils.formatDateTime(announcement.getCreatedAt()));
        txtStartDate.setText(DateUtils.formatDateTime(announcement.getStartDate()));
        txtEndDate.setText(DateUtils.formatDateTime(announcement.getEndDate()));
        
        // Tempo restante
        if (announcement.getEndDate() != null && announcement.getEndDate().getTime() > System.currentTimeMillis()) {
            String remaining = DateUtils.getTimeRemaining(announcement.getEndDate());
            txtTimeRemaining.setText("⏱️ Expira " + remaining);
            txtTimeRemaining.setVisibility(View.VISIBLE);
        } else {
            txtTimeRemaining.setVisibility(View.GONE);
        }
        
        // Badge de Status
        if (announcement.isActive()) {
            badgeStatus.setText("Ativo");
            badgeStatus.setBackgroundResource(R.drawable.bg_badge_orange);
        } else {
            badgeStatus.setText("Expirado");
            badgeStatus.setBackgroundResource(R.drawable.bg_badge_gray);
        }
        
        // Política de entrega
        String policyText;
        switch (announcement.getDeliveryPolicy()) {
            case "EVERYONE":
                policyText = "📋 Política: Todos os usuários";
                break;
            case "WHITELIST":
                policyText = "📋 Política: Apenas usuários específicos (Whitelist)";
                break;
            case "BLACKLIST":
                policyText = "📋 Política: Excluindo usuários específicos (Blacklist)";
                break;
            default:
                policyText = "📋 Política: " + announcement.getDeliveryPolicy();
        }
        txtPolicy.setText(policyText);
        
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
