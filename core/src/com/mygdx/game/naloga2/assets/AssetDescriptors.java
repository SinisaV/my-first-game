package com.mygdx.game.naloga2.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetDescriptors {
    public static final AssetDescriptor<BitmapFont> FONT =
            new AssetDescriptor<BitmapFont>(AssetPaths.FONT, BitmapFont.class);

    public static final AssetDescriptor<TextureAtlas> GAMEPLAY =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GAMEPLAY, TextureAtlas.class);

    public static final AssetDescriptor<Sound> BACKPACK_VOICE =
            new AssetDescriptor<>(AssetPaths.BACKPACK_VOICE, Sound.class);

    public static final AssetDescriptor<Sound> DUMBBELL_COLLECT =
            new AssetDescriptor<>(AssetPaths.DUMBBELL_COLLECT, Sound.class);

    public static final AssetDescriptor<ParticleEffect> POWER_UP =
            new AssetDescriptor<>(AssetPaths.POWER_UP, ParticleEffect.class);

    public static final AssetDescriptor<ParticleEffect> BLOOD =
            new AssetDescriptor<>(AssetPaths.BLOOD, ParticleEffect.class);

    private AssetDescriptors() {
    }
}
