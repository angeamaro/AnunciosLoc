import { motion } from 'motion/react';
import { MapPin, MessageSquare, Bell, TrendingUp, Calendar, Eye } from 'lucide-react';
import type { User } from '../App';
import type { Screen } from '../App';
import type { Announcement } from '../App';
import { Card } from './ui/card';
import { TopMenu } from './TopMenu';

interface MainActivityProps {
  user: User;
  onNavigate: (screen: Screen) => void;
  onLogout: () => void;
  totalLocations: number;
  totalAnnouncements: number;
  totalNotifications: number;
  recentAnnouncements: Announcement[];
  onViewAnnouncementDetails: (id: string) => void;
}

export function MainActivity({
  user,
  onNavigate,
  onLogout,
  totalLocations,
  totalAnnouncements,
  totalNotifications,
  recentAnnouncements,
  onViewAnnouncementDetails
}: MainActivityProps) {
  const stats = [
    { label: 'Locais', value: totalLocations, icon: MapPin, color: 'from-blue-500 to-blue-600', screen: 'listar-locais' as Screen },
    { label: 'Anúncios', value: totalAnnouncements, icon: MessageSquare, color: 'from-purple-500 to-purple-600', screen: 'gerir-anuncios' as Screen },
    { label: 'Notificações', value: totalNotifications, icon: Bell, color: 'from-orange-500 to-orange-600', screen: 'notificacoes' as Screen }
  ];

  return (
    <div className="h-full w-full bg-gradient-to-b from-gray-50 to-white flex flex-col overflow-y-auto pb-24">
      {/* Header com Gradiente */}
      <div className="bg-gradient-to-br from-blue-600 via-blue-500 to-purple-600 pt-14 pb-24 px-6 rounded-b-[40px] relative overflow-hidden">
        {/* Efeito de fundo */}
        <div className="absolute inset-0 opacity-20">
          <div className="absolute top-10 right-10 w-32 h-32 bg-white rounded-full blur-3xl" />
          <div className="absolute bottom-10 left-10 w-40 h-40 bg-purple-300 rounded-full blur-3xl" />
        </div>

        <div className="relative z-10">
          <motion.div
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            className="flex items-center justify-between mb-8"
          >
            <div>
              <p className="text-blue-100 text-sm">Bem-vindo de volta</p>
              <h1 className="text-white text-3xl mt-1">{user.name}</h1>
            </div>
            <TopMenu onNavigate={onNavigate} onLogout={onLogout} />
          </motion.div>

          {/* Stats Cards */}
          <div className="grid grid-cols-3 gap-3">
            {stats.map((stat, index) => (
              <motion.button
                key={stat.label}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.1 * index }}
                onClick={() => onNavigate(stat.screen)}
                className="bg-white/10 backdrop-blur-md rounded-2xl p-4 border border-white/20 hover:bg-white/20 transition-all"
              >
                <div className={`w-10 h-10 bg-gradient-to-br ${stat.color} rounded-xl flex items-center justify-center mb-3 mx-auto`}>
                  <stat.icon className="w-5 h-5 text-white" />
                </div>
                <p className="text-white text-2xl mb-1">{stat.value}</p>
                <p className="text-blue-100 text-xs">{stat.label}</p>
              </motion.button>
            ))}
          </div>
        </div>
      </div>

      {/* Conteúdo Principal */}
      <div className="flex-1 px-6 -mt-12">
        {/* Card de Atividade Recente */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
        >
          <Card className="bg-white rounded-3xl shadow-xl p-6 border-0 mb-6">
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center space-x-2">
                <TrendingUp className="w-5 h-5 text-blue-600" />
                <h2 className="text-gray-800 text-lg">Atividade Recente</h2>
              </div>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div className="bg-gradient-to-br from-blue-50 to-blue-100 rounded-2xl p-4">
                <p className="text-blue-600 text-sm mb-1">Esta Semana</p>
                <p className="text-blue-900 text-2xl">12</p>
                <p className="text-blue-600 text-xs">novos anúncios</p>
              </div>
              <div className="bg-gradient-to-br from-purple-50 to-purple-100 rounded-2xl p-4">
                <p className="text-purple-600 text-sm mb-1">Proximidade</p>
                <p className="text-purple-900 text-2xl">5</p>
                <p className="text-purple-600 text-xs">locais ativos</p>
              </div>
            </div>
          </Card>
        </motion.div>

        {/* Anúncios Recentes */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.4 }}
          className="mb-6"
        >
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-gray-800 text-lg">Anúncios Recentes</h2>
            <button
              onClick={() => onNavigate('gerir-anuncios')}
              className="text-blue-600 text-sm hover:text-blue-700"
            >
              Ver todos
            </button>
          </div>
          <div className="space-y-3">
            {recentAnnouncements.map((announcement, index) => (
              <motion.div
                key={announcement.id}
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: 0.1 * index }}
                className="bg-white rounded-2xl p-4 shadow-md border border-gray-100 hover:shadow-lg transition-shadow"
              >
                <div className="flex items-start justify-between mb-2">
                  <h3 className="text-gray-800 flex-1">{announcement.title}</h3>
                  <button
                    onClick={() => onViewAnnouncementDetails(announcement.id)}
                    className="ml-2 text-blue-600 hover:text-blue-700 flex items-center space-x-1 text-sm"
                  >
                    <Eye className="w-4 h-4" />
                    <span>Ver</span>
                  </button>
                </div>
                <p className="text-gray-600 text-sm line-clamp-2 mb-3">
                  {announcement.content}
                </p>
                <div className="flex items-center justify-between text-xs">
                  <div className="flex items-center space-x-1 text-gray-500">
                    <MapPin className="w-3 h-3" />
                    <span>{announcement.locationName}</span>
                  </div>
                  <div className="flex items-center space-x-1 text-gray-400">
                    <Calendar className="w-3 h-3" />
                    <span>{announcement.createdAt.toLocaleDateString('pt-PT')}</span>
                  </div>
                </div>
              </motion.div>
            ))}
          </div>
        </motion.div>
      </div>
    </div>
  );
}
