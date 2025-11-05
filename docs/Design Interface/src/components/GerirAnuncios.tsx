import { useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { MessageSquare, Trash2, MapPin, Eye, Bookmark, Calendar, User, Search } from 'lucide-react';
import { Button } from './ui/button';
import { Tabs, TabsList, TabsTrigger } from './ui/tabs';
import { Badge } from './ui/badge';
import { Input } from './ui/input';
import type { Announcement, Location } from '../App';
import type { Screen } from '../App';
import { TopMenu } from './TopMenu';

interface GerirAnunciosProps {
  announcements: Announcement[];
  savedAnnouncementIds: string[];
  locations: Location[];
  userEmail: string;
  onAdd: (announcement: Omit<Announcement, 'id' | 'createdAt' | 'authorEmail' | 'authorName'>) => void;
  onRemove: (id: string) => void;
  onViewDetails: (id: string) => void;
  onToggleSave: (id: string) => void;
  onNavigate: (screen: Screen) => void;
  onLogout: () => void;
}

export function GerirAnuncios({ 
  announcements, 
  savedAnnouncementIds,
  userEmail,
  onRemove, 
  onViewDetails,
  onToggleSave,
  onNavigate,
  onLogout
}: GerirAnunciosProps) {
  const [activeTab, setActiveTab] = useState<'my' | 'saved'>('saved');
  const [searchQuery, setSearchQuery] = useState('');

  // Mostrar apenas anúncios guardados
  const savedAnnouncements = announcements.filter(a => savedAnnouncementIds.includes(a.id));
  const myAnnouncements = announcements.filter(a => a.authorEmail === userEmail);
  
  const currentAnnouncements = activeTab === 'my' 
    ? myAnnouncements 
    : savedAnnouncements;
  
  const displayedAnnouncements = currentAnnouncements.filter(a => 
    a.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
    a.content.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="h-full w-full bg-gradient-to-b from-gray-50 to-white flex flex-col">
      {/* Header */}
      <div className="bg-gradient-to-br from-blue-600 via-blue-500 to-purple-600 pt-14 pb-12 px-6 rounded-b-[40px] relative overflow-hidden">
        <div className="absolute inset-0 opacity-20">
          <div className="absolute top-10 right-10 w-32 h-32 bg-white rounded-full blur-3xl" />
          <div className="absolute bottom-10 left-10 w-40 h-40 bg-purple-300 rounded-full blur-3xl" />
        </div>

        <div className="relative z-10">
          <div className="flex items-center justify-between mb-6">
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              className="flex-1"
            >
              <h1 className="text-white text-3xl mb-2">Anúncios</h1>
              <p className="text-blue-100">
                {displayedAnnouncements.length} {displayedAnnouncements.length === 1 ? 'anúncio' : 'anúncios'}
              </p>
            </motion.div>
            <TopMenu onNavigate={onNavigate} onLogout={onLogout} />
          </div>

          {/* Tabs */}
          <Tabs value={activeTab} onValueChange={(v) => setActiveTab(v as 'my' | 'saved')} className="w-full">
            <TabsList className="grid w-full grid-cols-2 bg-white/10 backdrop-blur-md border border-white/20 p-1 rounded-2xl">
              <TabsTrigger 
                value="saved" 
                className="rounded-xl data-[state=active]:bg-white data-[state=active]:text-blue-600 text-white transition-all"
              >
                Guardados
              </TabsTrigger>
              <TabsTrigger 
                value="my"
                className="rounded-xl data-[state=active]:bg-white data-[state=active]:text-blue-600 text-white transition-all"
              >
                Meus Anúncios
              </TabsTrigger>
            </TabsList>
          </Tabs>
        </div>
      </div>

      {/* Barra de Pesquisa */}
      <div className="px-6 pt-6">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
          <Input
            type="text"
            placeholder="Pesquisar anúncios..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-11 h-12 bg-white"
          />
        </div>
      </div>

      {/* Lista de Anúncios */}
      <div className="flex-1 px-6 pt-4 overflow-y-auto pb-24">
        <AnimatePresence mode="wait">
          {displayedAnnouncements.length === 0 ? (
            <motion.div
              key="empty"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="flex flex-col items-center justify-center h-full pb-20"
            >
              {activeTab === 'saved' ? (
                <>
                  <div className="w-24 h-24 bg-gradient-to-br from-orange-100 to-orange-200 rounded-3xl flex items-center justify-center mb-4">
                    <Bookmark className="w-12 h-12 text-orange-500" />
                  </div>
                  <p className="text-gray-500 text-center mb-2">Nenhum anúncio guardado</p>
                  <p className="text-gray-400 text-center text-sm px-8">
                    Guarde anúncios interessantes para acessar rapidamente
                  </p>
                </>
              ) : (
                <>
                  <div className="w-24 h-24 bg-gradient-to-br from-blue-100 to-blue-200 rounded-3xl flex items-center justify-center mb-4">
                    <MessageSquare className="w-12 h-12 text-blue-500" />
                  </div>
                  <p className="text-gray-500 text-center mb-2">
                    {activeTab === 'my' ? 'Nenhum anúncio criado ainda' : 'Nenhum anúncio guardado'}
                  </p>
                  <p className="text-gray-400 text-center text-sm px-8">
                    {activeTab === 'my' 
                      ? 'Use o botão + para criar seu primeiro anúncio'
                      : 'Guarde anúncios interessantes das notificações'}
                  </p>
                </>
              )}
            </motion.div>
          ) : (
            <motion.div
              key={activeTab}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              className="space-y-3 pb-6"
            >
              {displayedAnnouncements.map((announcement, index) => {
                const isSaved = savedAnnouncementIds.includes(announcement.id);
                
                return (
                  <motion.div
                    key={announcement.id}
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: 0.05 * index }}
                    className="bg-white rounded-2xl p-5 shadow-md hover:shadow-xl transition-all border border-gray-100"
                  >
                    <div className="flex items-start justify-between mb-3">
                      <div className="flex-1">
                        <div className="flex items-center space-x-2 mb-2">
                          <h3 className="text-gray-800 flex-1">{announcement.title}</h3>
                          <motion.button
                            whileTap={{ scale: 0.9 }}
                            onClick={() => onToggleSave(announcement.id)}
                            className={`p-2 rounded-full transition-colors ${
                              isSaved 
                                ? 'bg-orange-100 text-orange-600' 
                                : 'bg-gray-100 text-gray-400 hover:bg-orange-50 hover:text-orange-500'
                            }`}
                          >
                            <Bookmark className={`w-4 h-4 ${isSaved ? 'fill-orange-600' : ''}`} />
                          </motion.button>
                        </div>
                        <p className="text-gray-600 text-sm line-clamp-2 mb-3">
                          {announcement.content}
                        </p>
                      </div>
                    </div>

                    <div className="space-y-2 mb-3">
                      <div className="flex items-center space-x-1 text-gray-500 text-xs">
                        <MapPin className="w-3 h-3" />
                        <span className="truncate">{announcement.locationName}</span>
                      </div>
                      <div className="flex items-center space-x-1 text-gray-500 text-xs">
                        <User className="w-3 h-3" />
                        <span className="truncate">{announcement.authorName}</span>
                      </div>
                      <div className="flex items-center space-x-1 text-gray-400 text-xs">
                        <Calendar className="w-3 h-3" />
                        <span>{announcement.createdAt.toLocaleDateString('pt-PT')}</span>
                      </div>
                    </div>

                    <div className="flex items-center space-x-2">
                      <Button
                        onClick={() => onViewDetails(announcement.id)}
                        className="flex-1 bg-gradient-to-r from-blue-500 to-blue-600 hover:from-blue-600 hover:to-blue-700 text-white h-10"
                      >
                        <Eye className="w-4 h-4 mr-2" />
                        Ver Detalhes
                      </Button>
                      {announcement.authorEmail === userEmail && (
                        <Button
                          size="icon"
                          variant="ghost"
                          onClick={() => onRemove(announcement.id)}
                          className="text-red-500 hover:text-red-700 hover:bg-red-50 h-10 w-10"
                        >
                          <Trash2 className="w-4 h-4" />
                        </Button>
                      )}
                    </div>

                    {isSaved && (
                      <Badge className="mt-3 bg-orange-100 text-orange-700 hover:bg-orange-100">
                        Guardado
                      </Badge>
                    )}
                  </motion.div>
                );
              })}
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    </div>
  );
}
