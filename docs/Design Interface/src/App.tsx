import { useState, useEffect } from "react";
import { SplashScreen } from "./components/SplashScreen";
import { LoginScreen } from "./components/LoginScreen";
import { RegisterScreen } from "./components/RegisterScreen";
import { ResetPasswordScreen } from "./components/ResetPasswordScreen";
import { MainActivity } from "./components/MainActivity";
import { ListarLocais } from "./components/ListarLocais";
import { AdicionarLocais } from "./components/AdicionarLocais";
import { CriarAnuncio } from "./components/CriarAnuncio";
import { GerirAnuncios } from "./components/GerirAnuncios";
import { AnuncioDetalhes } from "./components/AnuncioDetalhes";
import { Notificacoes } from "./components/Notificacoes";
import { Politicas } from "./components/Politicas";
import { Definicoes } from "./components/Definicoes";
import { Perfil } from "./components/Perfil";
import { Interesses } from "./components/Interesses";
import { AlterarPassword } from "./components/AlterarPassword";
import { BottomNavigation } from "./components/BottomNavigation";

export type Screen =
  | "splash"
  | "login"
  | "register"
  | "reset-password"
  | "main"
  | "listar-locais"
  | "adicionar-locais"
  | "criar-anuncio"
  | "gerir-anuncios"
  | "anuncio-detalhes"
  | "notificacoes"
  | "politicas"
  | "definicoes"
  | "perfil"
  | "interesses"
  | "alterar-password";

export interface User {
  name: string;
  email: string;
  avatar?: string;
  age?: number;
  interests: string[];
}

export interface Location {
  id: string;
  name: string;
  type: "gps" | "wifi";
  coordinates?: { lat: number; lng: number };
  wifiName?: string;
  createdAt: Date;
}

export interface DeliveryPolicy {
  minAge?: number;
  maxAge?: number;
  requiredInterests?: string[];
}

export interface Announcement {
  id: string;
  title: string;
  content: string;
  locationId: string;
  locationName: string;
  authorEmail: string;
  authorName: string;
  createdAt: Date;
  expiresAt?: Date;
  deliveryPolicy?: DeliveryPolicy;
}

function App() {
  const [currentScreen, setCurrentScreen] =
    useState<Screen>("splash");
  const [user, setUser] = useState<User | null>(null);
  const [selectedAnnouncementId, setSelectedAnnouncementId] =
    useState<string | null>(null);
  const [savedAnnouncementIds, setSavedAnnouncementIds] =
    useState<string[]>([]);

  const [locations, setLocations] = useState<Location[]>([
    {
      id: "1",
      name: "Universidade de Lisboa",
      type: "gps",
      coordinates: { lat: 38.7564, lng: -9.1549 },
      createdAt: new Date("2025-01-15"),
    },
    {
      id: "2",
      name: "Centro Comercial Colombo",
      type: "wifi",
      wifiName: "CC_Colombo_WiFi",
      createdAt: new Date("2025-02-10"),
    },
    {
      id: "3",
      name: "Praça do Comércio",
      type: "gps",
      coordinates: { lat: 38.7076, lng: -9.1365 },
      createdAt: new Date("2025-02-20"),
    },
    {
      id: "4",
      name: "Instituto Superior Técnico",
      type: "wifi",
      wifiName: "IST_WiFi",
      createdAt: new Date("2025-03-01"),
    },
  ]);
  const [announcements, setAnnouncements] = useState<
    Announcement[]
  >([
    {
      id: "1",
      title: "Promoção de Livros",
      content:
        "Desconto de 20% em todos os livros técnicos até o final do mês! Aproveite esta oportunidade única para adquirir os melhores livros de programação, design e tecnologia.",
      locationId: "1",
      locationName: "Universidade de Lisboa",
      authorEmail: "livraria@exemplo.pt",
      authorName: "Livraria Técnica",
      createdAt: new Date("2025-03-01"),
      expiresAt: new Date("2025-03-31"),
      deliveryPolicy: {
        minAge: 18,
        requiredInterests: ["Tecnologia", "Programação"],
      },
    },
    {
      id: "2",
      title: "Evento de Tecnologia",
      content:
        "Workshop gratuito sobre desenvolvimento Android. Inscrições abertas! Venha aprender as melhores práticas e conhecer os novos recursos do Android.",
      locationId: "2",
      locationName: "Centro Comercial Colombo",
      authorEmail: "eventos@tech.pt",
      authorName: "Tech Events",
      createdAt: new Date("2025-03-15"),
      deliveryPolicy: {
        minAge: 16,
        requiredInterests: ["Tecnologia", "Programação"],
      },
    },
    {
      id: "3",
      title: "Aulas de Yoga",
      content:
        "Aulas de yoga ao ar livre todos os sábados às 9h. Traga seu tapete e venha relaxar!",
      locationId: "3",
      locationName: "Praça do Comércio",
      authorEmail: "yoga@wellness.pt",
      authorName: "Wellness Studio",
      createdAt: new Date("2025-03-10"),
      expiresAt: new Date("2025-04-30"),
      deliveryPolicy: {
        minAge: 18,
        maxAge: 65,
        requiredInterests: ["Desporto", "Fitness"],
      },
    },
    {
      id: "4",
      title: "Curso de Fotografia",
      content:
        "Aprenda técnicas profissionais de fotografia. Curso de 4 semanas com certificado.",
      locationId: "1",
      locationName: "Universidade de Lisboa",
      authorEmail: "joao.silva@exemplo.pt",
      authorName: "João Silva",
      createdAt: new Date("2025-03-20"),
      deliveryPolicy: {
        minAge: 16,
        requiredInterests: ["Fotografia", "Arte"],
      },
    },
  ]);

  useEffect(() => {
    // Simula splash screen por 2.5 segundos
    const timer = setTimeout(() => {
      setCurrentScreen("login");
    }, 2500);

    return () => clearTimeout(timer);
  }, []);

  const handleLogin = (email: string, password: string) => {
    // Simula login
    setUser({
      name: "João Silva",
      email: email,
      age: 25,
      interests: [
        "Tecnologia",
        "Programação",
        "Design",
        "Fotografia",
      ],
    });
    setCurrentScreen("main");
  };

  const handleRegister = (
    name: string,
    email: string,
    password: string,
  ) => {
    // Simula registro - não navega automaticamente porque o RegisterScreen agora lida com isso
    setUser({
      name: name,
      email: email,
      age: 25,
      interests: ["Tecnologia", "Programação"],
    });
  };

  const handleLogout = () => {
    setUser(null);
    setCurrentScreen("login");
  };

  const addLocation = (
    location: Omit<Location, "id" | "createdAt">,
  ) => {
    const newLocation: Location = {
      ...location,
      id: Date.now().toString(),
      createdAt: new Date(),
    };
    setLocations([...locations, newLocation]);
  };

  const removeLocation = (id: string) => {
    setLocations(locations.filter((loc) => loc.id !== id));
  };

  // Função para verificar se um anúncio corresponde ao perfil do utilizador
  const matchesUserProfile = (
    announcement: Announcement,
    user: User,
  ): boolean => {
    if (!announcement.deliveryPolicy) return true;

    const policy = announcement.deliveryPolicy;

    // Verificar idade
    if (user.age) {
      if (policy.minAge && user.age < policy.minAge)
        return false;
      if (policy.maxAge && user.age > policy.maxAge)
        return false;
    }

    // Verificar interesses
    if (
      policy.requiredInterests &&
      policy.requiredInterests.length > 0
    ) {
      const hasRequiredInterest = policy.requiredInterests.some(
        (interest) => user.interests.includes(interest),
      );
      if (!hasRequiredInterest) return false;
    }

    return true;
  };

  const addAnnouncement = (
    announcement: Omit<
      Announcement,
      "id" | "createdAt" | "authorEmail" | "authorName"
    >,
  ) => {
    if (!user) return;

    const newAnnouncement: Announcement = {
      ...announcement,
      id: Date.now().toString(),
      createdAt: new Date(),
      authorEmail: user.email,
      authorName: user.name,
    };
    setAnnouncements([...announcements, newAnnouncement]);
    // Auto-guardar anúncios criados pelo utilizador
    setSavedAnnouncementIds([
      ...savedAnnouncementIds,
      newAnnouncement.id,
    ]);
  };

  const removeAnnouncement = (id: string) => {
    const announcement = announcements.find((a) => a.id === id);
    // Só pode remover se for o autor
    if (
      announcement &&
      announcement.authorEmail === user?.email
    ) {
      setAnnouncements(
        announcements.filter((ann) => ann.id !== id),
      );
      setSavedAnnouncementIds(
        savedAnnouncementIds.filter((aid) => aid !== id),
      );
    }
  };

  const toggleSaveAnnouncement = (id: string) => {
    if (savedAnnouncementIds.includes(id)) {
      setSavedAnnouncementIds(
        savedAnnouncementIds.filter((aid) => aid !== id),
      );
    } else {
      setSavedAnnouncementIds([...savedAnnouncementIds, id]);
    }
  };

  const handleViewAnnouncementDetails = (
    announcementId: string,
  ) => {
    setSelectedAnnouncementId(announcementId);
    setCurrentScreen("anuncio-detalhes");
  };

  const shouldShowBottomNav =
    user &&
    ![
      "splash",
      "login",
      "register",
      "reset-password",
      "adicionar-locais",
      "criar-anuncio",
      "anuncio-detalhes",
      "politicas",
      "perfil",
      "interesses",
      "definicoes",
      "alterar-password",
    ].includes(currentScreen);

  const renderScreen = () => {
    switch (currentScreen) {
      case "splash":
        return <SplashScreen />;
      case "login":
        return (
          <LoginScreen
            onLogin={handleLogin}
            onNavigateToRegister={() =>
              setCurrentScreen("register")
            }
            onNavigateToReset={() =>
              setCurrentScreen("reset-password")
            }
          />
        );
      case "register":
        return (
          <RegisterScreen
            onRegister={handleRegister}
            onNavigateToLogin={() => setCurrentScreen("login")}
            onNavigateToPolicies={() =>
              setCurrentScreen("politicas")
            }
          />
        );
      case "reset-password":
        return (
          <ResetPasswordScreen
            onBack={() => setCurrentScreen("login")}
          />
        );
      case "main":
        const matchingAnnouncements = announcements.filter(
          (a) => matchesUserProfile(a, user!),
        );
        return (
          <MainActivity
            user={user!}
            onNavigate={setCurrentScreen}
            onLogout={handleLogout}
            totalLocations={locations.length}
            totalAnnouncements={savedAnnouncementIds.length}
            totalNotifications={matchingAnnouncements.length}
            recentAnnouncements={matchingAnnouncements.slice(
              0,
              3,
            )}
            onViewAnnouncementDetails={
              handleViewAnnouncementDetails
            }
          />
        );
      case "listar-locais":
        return (
          <ListarLocais
            locations={locations}
            userEmail={user!.email}
            onRemove={removeLocation}
            onNavigateToAdd={() =>
              setCurrentScreen("adicionar-locais")
            }
            onNavigate={setCurrentScreen}
            onLogout={handleLogout}
          />
        );
      case "adicionar-locais":
        return (
          <AdicionarLocais
            onAdd={addLocation}
            onBack={() => setCurrentScreen("listar-locais")}
          />
        );
      case "criar-anuncio":
        return (
          <CriarAnuncio
            locations={locations}
            onAdd={addAnnouncement}
            onBack={() => setCurrentScreen("main")}
          />
        );
      case "gerir-anuncios":
        return (
          <GerirAnuncios
            announcements={announcements}
            savedAnnouncementIds={savedAnnouncementIds}
            locations={locations}
            userEmail={user!.email}
            onAdd={addAnnouncement}
            onRemove={removeAnnouncement}
            onViewDetails={handleViewAnnouncementDetails}
            onToggleSave={toggleSaveAnnouncement}
            onNavigate={setCurrentScreen}
            onLogout={handleLogout}
          />
        );
      case "anuncio-detalhes":
        const selectedAnnouncement = announcements.find(
          (a) => a.id === selectedAnnouncementId,
        );
        if (!selectedAnnouncement) {
          setCurrentScreen("gerir-anuncios");
          return null;
        }
        return (
          <AnuncioDetalhes
            announcement={selectedAnnouncement}
            isSaved={savedAnnouncementIds.includes(
              selectedAnnouncement.id,
            )}
            onBack={() => setCurrentScreen("gerir-anuncios")}
            onToggleSave={toggleSaveAnnouncement}
          />
        );
      case "notificacoes":
        return (
          <Notificacoes
            announcements={announcements}
            user={user!}
            savedAnnouncementIds={savedAnnouncementIds}
            onToggleSave={toggleSaveAnnouncement}
            onViewDetails={handleViewAnnouncementDetails}
            onNavigate={setCurrentScreen}
            onLogout={handleLogout}
          />
        );
      case "politicas":
        return (
          <Politicas
            onBack={() => {
              // Volta para a tela anterior (registro ou main)
              if (currentScreen === "politicas" && !user) {
                setCurrentScreen("register");
              } else {
                setCurrentScreen("main");
              }
            }}
          />
        );
      case "definicoes":
        return (
          <Definicoes
            user={user!}
            onSave={(updatedUser) => setUser(updatedUser)}
            onBack={() => setCurrentScreen("main")}
            onNavigate={setCurrentScreen}
          />
        );
      case "perfil":
        return (
          <Perfil
            user={user!}
            onSave={(updatedUser) => setUser(updatedUser)}
            onBack={() => setCurrentScreen("main")}
            onAlterarPassword={() =>
              setCurrentScreen("alterar-password")
            }
          />
        );
      case "interesses":
        return (
          <Interesses onBack={() => setCurrentScreen("main")} />
        );
      case "alterar-password":
        return (
          <AlterarPassword
            onBack={() => setCurrentScreen("perfil")}
          />
        );
      default:
        return <SplashScreen />;
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
      <div className="w-full max-w-[400px] h-[800px] bg-white rounded-3xl shadow-2xl overflow-hidden relative">
        {renderScreen()}

        {shouldShowBottomNav && (
          <BottomNavigation
            currentScreen={currentScreen}
            onNavigate={setCurrentScreen}
            onCreateAnnouncement={() =>
              setCurrentScreen("criar-anuncio")
            }
          />
        )}
      </div>
    </div>
  );
}

export default App;