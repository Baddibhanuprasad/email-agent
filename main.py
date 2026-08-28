"""
Main entry point for Virus Attacks Detection System - Background Service
"""
import sys
import os
import logging
import threading
import time
from pathlib import Path

# Add the current directory to Python path
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

def setup_environment():
    """Initialize directories and logging"""
    dirs = ['logs', 'quarantine/temp', 'quarantine/scanned', 
            'quarantine/malicious', 'quarantine/released', 'rules', 'config']
    
    for dir_name in dirs:
        Path(dir_name).mkdir(parents=True, exist_ok=True)
        print(f"Created directory: {dir_name}")
    
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
        handlers=[
            logging.FileHandler('logs/system_events.log'),
            logging.StreamHandler()
        ]
    )
    
    return logging.getLogger(__name__)

def run_background_service():
    """Run as background service (like Windows Defender)"""
    logger = logging.getLogger(__name__)
    logger.info("Starting background service mode...")
    
    try:
        from file_detector import AppDownloadMonitor
        from quarantine_system import QuarantineSandbox
        
        # Initialize components
        quarantine = QuarantineSandbox(auto_release_days=2)
        monitor = AppDownloadMonitor()
        
        # Start monitoring
        monitor.start_monitoring()
        
        logger.info("✅ Background service running - Protecting your system")
        logger.info("📁 Monitoring: Downloads, USB drives, WhatsApp, Telegram, Gmail")
        logger.info("🛡️ Auto-sandbox: ON")
        
        # Keep running
        while True:
            time.sleep(60)
            
    except KeyboardInterrupt:
        logger.info("Service stopped by user")
    except Exception as e:
        logger.error(f"Service error: {e}")

def run_with_ui():
    """Run with full UI"""
    logger = logging.getLogger(__name__)
    logger.info("Starting UI mode...")
    
    try:
        from ui_interface import VirusDetectionApp
        from config import Config
        
        config = Config()
        app = VirusDetectionApp(config)
        app.run()
        
    except Exception as e:
        logger.error(f"UI failed to start: {e}")
        import traceback
        traceback.print_exc()

def main():
    """Main application entry point"""
    logger = setup_environment()
    logger.info("Environment setup complete")
    
    # Check for command line arguments
    if len(sys.argv) > 1:
        if sys.argv[1] == '--service' or sys.argv[1] == '-s':
            # Run as background service
            run_background_service()
            return
        elif sys.argv[1] == '--help' or sys.argv[1] == '-h':
            print("""
🛡️ Virus Attacks Detection System

Usage:
  python main.py              - Run with full UI
  python main.py --service    - Run as background service (like Windows Defender)
  python main.py --help       - Show this help

Features:
  ✅ Auto-detect USB drives
  ✅ Monitor WhatsApp, Telegram, Gmail downloads
  ✅ Auto-open files in Sandboxie
  ✅ 2-day auto-release for safe files
  ✅ Complete isolation from main system
""")
            return
    
    # Run with UI by default
    run_with_ui()

if __name__ == "__main__":
    main()