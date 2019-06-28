package de.tub.ise.anwsys.controllers;

class ChannelNotFoundException extends RuntimeException{

    ChannelNotFoundException(Long id) {
        super(String.format("Channel %d does not exist!", id));
    }

}