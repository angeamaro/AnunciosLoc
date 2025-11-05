import { motion } from 'motion/react';
import { MapPin, Wifi } from 'lucide-react';

export function SplashScreen() {
  return (
    <div className="h-full w-full bg-gradient-to-br from-blue-600 via-blue-700 to-blue-900 flex flex-col items-center justify-center">
      <motion.div
        initial={{ scale: 0, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        transition={{ duration: 0.5, ease: 'easeOut' }}
        className="relative"
      >
        <div className="w-32 h-32 bg-white rounded-3xl flex items-center justify-center shadow-2xl">
          <div className="relative">
            <MapPin className="w-16 h-16 text-blue-600" strokeWidth={2.5} />
            <Wifi className="w-8 h-8 text-blue-500 absolute -bottom-2 -right-2" />
          </div>
        </div>
      </motion.div>

      <motion.h1
        initial={{ y: 20, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ delay: 0.3, duration: 0.5 }}
        className="mt-8 text-white text-4xl tracking-tight"
      >
        AnunciosLoc
      </motion.h1>

      <motion.p
        initial={{ y: 20, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ delay: 0.5, duration: 0.5 }}
        className="mt-3 text-blue-200 text-center px-8"
      >
        Anúncios baseados em localização
      </motion.p>

      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 1, duration: 0.5 }}
        className="absolute bottom-12"
      >
        <div className="flex space-x-2">
          <div className="w-2 h-2 bg-white rounded-full animate-pulse" style={{ animationDelay: '0ms' }} />
          <div className="w-2 h-2 bg-white rounded-full animate-pulse" style={{ animationDelay: '150ms' }} />
          <div className="w-2 h-2 bg-white rounded-full animate-pulse" style={{ animationDelay: '300ms' }} />
        </div>
      </motion.div>
    </div>
  );
}
