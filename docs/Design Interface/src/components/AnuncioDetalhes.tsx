import { motion } from 'motion/react';
import { ArrowLeft, MapPin, Calendar, User, Mail, Phone, Bookmark, BookmarkCheck, Shield } from 'lucide-react';
import { Button } from './ui/button';
import { Badge } from './ui/badge';
import { Separator } from './ui/separator';
import type { Announcement } from '../App';
import { toast } from 'sonner@2.0.3';

interface AnuncioDetalhesProps {
  announcement: Announcement;
  isSaved: boolean;
  onBack: () => void;
  onToggleSave: (id: string) => void;
}

export function AnuncioDetalhes({ announcement, isSaved, onBack, onToggleSave }: AnuncioDetalhesProps) {
  const handleToggleSave = () => {
    onToggleSave(announcement.id);
    toast(isSaved ? 'Anúncio removido dos guardados' : 'Anúncio guardado com sucesso!');
  };

  return (
    <div className="h-full w-full bg-gradient-to-b from-gray-50 to-white flex flex-col">
      {/* Header com imagem de fundo */}
      <div className="relative bg-gradient-to-br from-blue-600 via-blue-500 to-purple-600 pt-12 pb-32 px-6">
        <div className="flex items-center justify-between mb-6">
          <button
            onClick={onBack}
            className="text-white flex items-center justify-center w-10 h-10 bg-white/20 backdrop-blur-sm rounded-full hover:bg-white/30 transition-colors"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <motion.button
            whileTap={{ scale: 0.9 }}
            onClick={handleToggleSave}
            className="text-white flex items-center justify-center w-10 h-10 bg-white/20 backdrop-blur-sm rounded-full hover:bg-white/30 transition-colors"
          >
            {isSaved ? (
              <BookmarkCheck className="w-5 h-5 fill-white" />
            ) : (
              <Bookmark className="w-5 h-5" />
            )}
          </motion.button>
        </div>
        
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <Badge className="bg-white/20 text-white backdrop-blur-sm border-white/30 mb-3">
            Anúncio
          </Badge>
          <h1 className="text-white text-3xl mb-3">
            {announcement.title}
          </h1>
          <div className="flex items-center space-x-2 text-white/90 text-sm">
            <Calendar className="w-4 h-4" />
            <span>Publicado em {announcement.createdAt.toLocaleDateString('pt-PT')}</span>
          </div>
        </motion.div>
      </div>

      {/* Card principal com conteúdo */}
      <div className="flex-1 -mt-20 px-6 overflow-y-auto pb-24">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="bg-white rounded-3xl shadow-2xl p-6 space-y-6"
        >
          {/* Localização */}
          <div>
            <div className="flex items-center space-x-2 text-gray-500 mb-2">
              <MapPin className="w-5 h-5 text-blue-600" />
              <span>Localização</span>
            </div>
            <p className="text-gray-800 text-lg">{announcement.locationName}</p>
          </div>

          <Separator />

          {/* Descrição */}
          <div>
            <h3 className="text-gray-500 mb-3">Descrição</h3>
            <p className="text-gray-800 leading-relaxed">
              {announcement.content}
            </p>
          </div>

          <Separator />

          {/* Informações do Publicador */}
          <div>
            <h3 className="text-gray-500 mb-4">Publicado por</h3>
            <div className="space-y-3">
              <div className="flex items-center space-x-3">
                <div className="w-12 h-12 bg-gradient-to-br from-blue-500 to-purple-500 rounded-full flex items-center justify-center">
                  <User className="w-6 h-6 text-white" />
                </div>
                <div>
                  <p className="text-gray-800">{announcement.authorName}</p>
                  <p className="text-gray-500 text-sm">{announcement.authorEmail}</p>
                </div>
              </div>
            </div>
          </div>

          {announcement.deliveryPolicy && (
            <>
              <Separator />
              
              {/* Política de Entrega */}
              <div>
                <div className="flex items-center space-x-2 text-gray-500 mb-3">
                  <Shield className="w-5 h-5 text-blue-600" />
                  <h3>Público-Alvo</h3>
                </div>
                <div className="space-y-2">
                  {announcement.deliveryPolicy.minAge && (
                    <div className="flex items-center space-x-2">
                      <Badge variant="secondary">
                        Idade mínima: {announcement.deliveryPolicy.minAge} anos
                      </Badge>
                    </div>
                  )}
                  {announcement.deliveryPolicy.maxAge && (
                    <div className="flex items-center space-x-2">
                      <Badge variant="secondary">
                        Idade máxima: {announcement.deliveryPolicy.maxAge} anos
                      </Badge>
                    </div>
                  )}
                  {announcement.deliveryPolicy.requiredInterests && announcement.deliveryPolicy.requiredInterests.length > 0 && (
                    <div>
                      <p className="text-gray-600 text-sm mb-2">Interesses:</p>
                      <div className="flex flex-wrap gap-2">
                        {announcement.deliveryPolicy.requiredInterests.map((interest, index) => (
                          <Badge key={index} className="bg-blue-100 text-blue-700 hover:bg-blue-100">
                            {interest}
                          </Badge>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </>
          )}

          <Separator />

          {/* Informações de Contato */}
          <div>
            <h3 className="text-gray-500 mb-4">Informações de Contato</h3>
            <div className="space-y-3">
              <a 
                href={`mailto:${announcement.authorEmail}`}
                className="flex items-center space-x-3 p-3 bg-gray-50 rounded-xl hover:bg-gray-100 transition-colors"
              >
                <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
                  <Mail className="w-5 h-5 text-blue-600" />
                </div>
                <div className="flex-1">
                  <p className="text-gray-600 text-sm">Email</p>
                  <p className="text-gray-800">{announcement.authorEmail}</p>
                </div>
              </a>
              
              <a 
                href="tel:+351912345678" 
                className="flex items-center space-x-3 p-3 bg-gray-50 rounded-xl hover:bg-gray-100 transition-colors"
              >
                <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                  <Phone className="w-5 h-5 text-green-600" />
                </div>
                <div className="flex-1">
                  <p className="text-gray-600 text-sm">Telefone</p>
                  <p className="text-gray-800">+351 912 345 678</p>
                </div>
              </a>
            </div>
          </div>

          {/* Informações Adicionais */}
          <div className="bg-blue-50 rounded-xl p-4 border border-blue-100">
            <h3 className="text-blue-800 mb-2">Informações Adicionais</h3>
            <p className="text-blue-600 text-sm">
              Este anúncio estará ativo até {announcement.expiresAt?.toLocaleDateString('pt-PT') || 'data indefinida'}. 
              Para mais informações, entre em contato diretamente com o publicador.
            </p>
          </div>
        </motion.div>
      </div>
    </div>
  );
}
