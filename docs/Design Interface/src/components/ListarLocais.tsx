import { useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { MapPin, Wifi, Trash2, Plus, Globe, Search, Edit2 } from 'lucide-react';
import type { Location } from '../App';
import type { Screen } from '../App';
import { Button } from './ui/button';
import { Tabs, TabsList, TabsTrigger, TabsContent } from './ui/tabs';
import { Input } from './ui/input';
import { TopMenu } from './TopMenu';

interface ListarLocaisProps {
  locations: Location[];
  userEmail: string;
  onRemove: (id: string) => void;
  onNavigateToAdd: () => void;
  onNavigate: (screen: Screen) => void;
  onLogout: () => void;
}

export function ListarLocais({ locations, userEmail, onRemove, onNavigateToAdd, onNavigate, onLogout }: ListarLocaisProps) {
  const [activeTab, setActiveTab] = useState<'my' | 'all'>('my');
  const [searchQuery, setSearchQuery] = useState('');

  // Simula que locais com id 1 e 2 são do usuário
  const myLocations = locations.filter(loc => ['1', '2'].includes(loc.id));
  const otherLocations = locations.filter(loc => !['1', '2'].includes(loc.id));

  const currentLocations = activeTab === 'my' ? myLocations : otherLocations;
  
  const displayedLocations = currentLocations.filter(loc => 
    loc.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="h-full w-full bg-gradient-to-b from-gray-50 to-white flex flex-col">
      {/* Header Moderno */}
      <div className="bg-gradient-to-br from-blue-600 via-blue-500 to-purple-600 pt-14 pb-12 px-6 rounded-b-[40px] relative overflow-hidden">
        <div className="absolute inset-0 opacity-20">
          <div className="absolute top-10 right-10 w-32 h-32 bg-white rounded-full blur-3xl" />
        </div>
        
        <div className="relative z-10">
          <div className="flex items-center justify-between mb-6">
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              className="flex-1"
            >
              <h1 className="text-white text-3xl mb-2">Locais</h1>
              <p className="text-blue-100">
                {displayedLocations.length} {displayedLocations.length === 1 ? 'local' : 'locais'}
              </p>
            </motion.div>
            <div className="flex items-center space-x-2">
              <motion.button
                whileTap={{ scale: 0.9 }}
                onClick={onNavigateToAdd}
                className="w-12 h-12 bg-white/20 backdrop-blur-md rounded-full flex items-center justify-center border-2 border-white/30 hover:bg-white/30 transition-all shadow-lg"
              >
                <Plus className="w-6 h-6 text-white" strokeWidth={2.5} />
              </motion.button>
              <TopMenu onNavigate={onNavigate} onLogout={onLogout} />
            </div>
          </div>

          {/* Tabs */}
          <Tabs value={activeTab} onValueChange={(v) => setActiveTab(v as 'my' | 'all')} className="w-full">
            <TabsList className="grid w-full grid-cols-2 bg-white/10 backdrop-blur-md border border-white/20 p-1 rounded-2xl">
              <TabsTrigger 
                value="my" 
                className="rounded-xl data-[state=active]:bg-white data-[state=active]:text-blue-600 text-white transition-all"
              >
                Meus Locais
              </TabsTrigger>
              <TabsTrigger 
                value="all"
                className="rounded-xl data-[state=active]:bg-white data-[state=active]:text-blue-600 text-white transition-all"
              >
                Outros Locais
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
            placeholder="Pesquisar locais..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-11 h-12 bg-white"
          />
        </div>
      </div>

      {/* Lista de Locais */}
      <div className="flex-1 px-6 pt-4 overflow-y-auto pb-24">
        <AnimatePresence mode="wait">
          {displayedLocations.length === 0 ? (
            <motion.div
              key="empty"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="flex flex-col items-center justify-center h-full pb-20"
            >
              {activeTab === 'my' ? (
                <>
                  <div className="w-24 h-24 bg-gradient-to-br from-blue-100 to-blue-200 rounded-3xl flex items-center justify-center mb-4">
                    <MapPin className="w-12 h-12 text-blue-500" />
                  </div>
                  <p className="text-gray-500 text-center mb-2">Nenhum local cadastrado ainda</p>
                  <p className="text-gray-400 text-center text-sm px-8">
                    Clique no botão + para adicionar seu primeiro local
                  </p>
                </>
              ) : (
                <>
                  <div className="w-24 h-24 bg-gradient-to-br from-purple-100 to-purple-200 rounded-3xl flex items-center justify-center mb-4">
                    <Globe className="w-12 h-12 text-purple-500" />
                  </div>
                  <p className="text-gray-500 text-center">Nenhum local disponível</p>
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
              {displayedLocations.map((location, index) => {
                const isMine = myLocations.some(loc => loc.id === location.id);
                
                return (
                  <motion.div
                    key={location.id}
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: 0.05 * index }}
                    className="bg-white rounded-2xl p-5 shadow-md hover:shadow-xl transition-all border border-gray-100"
                  >
                    <div className="flex items-start space-x-4">
                      <div className={`${location.type === 'gps' ? 'bg-gradient-to-br from-blue-500 to-blue-600' : 'bg-gradient-to-br from-purple-500 to-purple-600'} w-14 h-14 rounded-2xl flex items-center justify-center flex-shrink-0 shadow-lg`}>
                        {location.type === 'gps' ? (
                          <MapPin className="w-7 h-7 text-white" />
                        ) : (
                          <Wifi className="w-7 h-7 text-white" />
                        )}
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-start justify-between mb-2">
                          <h3 className="text-gray-800">{location.name}</h3>
                          {isMine && activeTab === 'my' && (
                            <div className="flex items-center space-x-1">
                              <Button
                                size="icon"
                                variant="ghost"
                                onClick={() => {/* TODO: Implementar edição */}}
                                className="text-blue-500 hover:text-blue-700 hover:bg-blue-50 -mt-1 h-8 w-8"
                              >
                                <Edit2 className="w-4 h-4" />
                              </Button>
                              <Button
                                size="icon"
                                variant="ghost"
                                onClick={() => onRemove(location.id)}
                                className="text-red-500 hover:text-red-700 hover:bg-red-50 -mt-1 -mr-2 h-8 w-8"
                              >
                                <Trash2 className="w-4 h-4" />
                              </Button>
                            </div>
                          )}
                        </div>
                        <div className="flex items-center space-x-2 mb-2">
                          <span className={`text-xs px-2 py-1 rounded-full ${location.type === 'gps' ? 'bg-blue-100 text-blue-700' : 'bg-purple-100 text-purple-700'}`}>
                            {location.type === 'gps' ? 'GPS' : 'Wi-Fi'}
                          </span>
                          {isMine && (
                            <span className="text-xs px-2 py-1 rounded-full bg-green-100 text-green-700">
                              Meu Local
                            </span>
                          )}
                        </div>
                        <p className="text-gray-500 text-sm">
                          {location.type === 'gps' ? (
                            `${location.coordinates?.lat.toFixed(4)}, ${location.coordinates?.lng.toFixed(4)}`
                          ) : (
                            `${location.wifiName}`
                          )}
                        </p>
                        <p className="text-gray-400 text-xs mt-2">
                          Criado em {location.createdAt.toLocaleDateString('pt-PT')}
                        </p>
                      </div>
                    </div>
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
