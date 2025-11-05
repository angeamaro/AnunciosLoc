import { motion } from 'motion/react';
import { Bell, MapPin, User, Bookmark, Eye } from 'lucide-react';
import type { Announcement, User as UserType } from '../App';
import type { Screen } from '../App';
import { Badge } from './ui/badge';
import { Button } from './ui/button';
import { TopMenu } from './TopMenu';

interface NotificacoesProps {
  announcements: Announcement[];
  user: UserType;
  savedAnnouncementIds: string[];
  onToggleSave: (id: string) => void;
  onViewDetails: (id: string) => void;
  onNavigate: (screen: Screen) => void;
  onLogout: () => void;
}

export function Notificacoes({ announcements, user, savedAnnouncementIds, onToggleSave, onViewDetails, onNavigate, onLogout }: NotificacoesProps) {
  // Função para verificar se anúncio corresponde ao perfil
  const matchesUserProfile = (announcement: Announcement): boolean => {
    if (!announcement.deliveryPolicy) return true;
    
    const policy = announcement.deliveryPolicy;
    
    // Verificar idade
    if (user.age) {
      if (policy.minAge && user.age < policy.minAge) return false;
      if (policy.maxAge && user.age > policy.maxAge) return false;
    }
    
    // Verificar interesses
    if (policy.requiredInterests && policy.requiredInterests.length > 0) {
      const hasRequiredInterest = policy.requiredInterests.some(
        interest => user.interests.includes(interest)
      );
      if (!hasRequiredInterest) return false;
    }
    
    return true;
  };

  // Filtrar apenas anúncios que correspondem ao perfil
  const filteredAnnouncements = announcements.filter(matchesUserProfile);
  return (
    <div className="h-full w-full bg-gradient-to-b from-gray-50 to-white flex flex-col">
      {/* Header */}
      <div className="bg-gradient-to-br from-orange-500 via-orange-600 to-red-600 pt-14 pb-12 px-6 rounded-b-[40px] relative overflow-hidden">
        <div className="absolute inset-0 opacity-20">
          <div className="absolute top-10 right-10 w-32 h-32 bg-white rounded-full blur-3xl" />
        </div>
        
        <div className="relative z-10">
          <div className="flex items-center justify-between">
            <motion.div
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ duration: 0.3 }}
              className="flex-1"
            >
              <h1 className="text-white text-3xl mb-2">Notificações</h1>
              <p className="text-orange-100">Anúncios próximos a você</p>
            </motion.div>
            <TopMenu onNavigate={onNavigate} onLogout={onLogout} />
          </div>
        </div>
      </div>

      {/* Lista de Notificações */}
      <div className="flex-1 px-6 pt-6 overflow-y-auto pb-24">
        {filteredAnnouncements.length === 0 ? (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="flex flex-col items-center justify-center h-full pb-20"
          >
            <div className="w-24 h-24 bg-gradient-to-br from-orange-100 to-orange-200 rounded-3xl flex items-center justify-center mb-4">
              <Bell className="w-12 h-12 text-orange-500" />
            </div>
            <p className="text-gray-500 text-center mb-2">
              Nenhuma notificação no momento
            </p>
            <p className="text-gray-400 text-center text-sm px-8">
              Você receberá alertas quando estiver próximo a locais com anúncios
            </p>
          </motion.div>
        ) : (
          <div className="space-y-3 pb-6">
            {filteredAnnouncements.map((announcement, index) => {
              const isSaved = savedAnnouncementIds.includes(announcement.id);
              
              return (
                <motion.div
                  key={announcement.id}
                  initial={{ opacity: 0, x: -20 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: 0.05 * index }}
                  className="bg-white rounded-2xl p-5 shadow-md hover:shadow-xl transition-all border-l-4 border-orange-500"
                >
                  <div className="flex items-start justify-between mb-2">
                    <h3 className="text-gray-800 flex-1">{announcement.title}</h3>
                    <div className="flex items-center space-x-2">
                      <Badge className="bg-gradient-to-r from-orange-500 to-red-500 text-white hover:from-orange-600 hover:to-red-600">
                        Novo
                      </Badge>
                    </div>
                  </div>
                  <p className="text-gray-600 text-sm mb-3 line-clamp-2">{announcement.content}</p>
                  
                  <div className="space-y-2 mb-3">
                    <div className="flex items-center space-x-2 text-gray-500 text-xs">
                      <MapPin className="w-4 h-4" />
                      <span>{announcement.locationName}</span>
                    </div>
                    <div className="flex items-center space-x-2 text-gray-500 text-xs">
                      <User className="w-4 h-4" />
                      <span>{announcement.authorName}</span>
                    </div>
                  </div>

                  <div className="flex items-center justify-between">
                    <span className="text-gray-400 text-xs">
                      {announcement.createdAt.toLocaleDateString('pt-PT')}
                    </span>
                    <div className="flex items-center space-x-2">
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => onViewDetails(announcement.id)}
                        className="border-blue-500 text-blue-600 hover:bg-blue-50"
                      >
                        <Eye className="w-4 h-4 mr-1" />
                        Ver Mais
                      </Button>
                      <Button
                        size="sm"
                        variant={isSaved ? "default" : "outline"}
                        onClick={() => onToggleSave(announcement.id)}
                        className={isSaved 
                          ? "bg-orange-500 hover:bg-orange-600 text-white" 
                          : "border-orange-500 text-orange-600 hover:bg-orange-50"}
                      >
                        <Bookmark className={`w-4 h-4 mr-1 ${isSaved ? 'fill-white' : ''}`} />
                        {isSaved ? 'Guardado' : 'Guardar'}
                      </Button>
                    </div>
                  </div>
                </motion.div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}
