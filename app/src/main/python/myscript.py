def main(s: str):
    return f"Hello, {s} World"

from pydub import AudioSegment
from tqdm import tqdm
import os
AudioSegment.converter = "/usr/local/Cellar/ffmpeg/5.1.2/bin/ffmpeg"
AudioSegment.ffmpeg = "/usr/local/Cellar/ffmpeg/5.1.2/bin/ffmpeg"
AudioSegment.ffprobe = "/usr/local/Cellar/ffmpeg/5.1.2/bin/ffprobe"

def concatenate_audio_pydub(audio_clip_paths, output_path, verbose=1):
    """
    Concatenates two or more audio files into one audio file using PyDub library
    and save it to `output_path`. A lot of extensions are supported, more on PyDub's doc.
    """
    def get_file_extension(filename):
        """A helper function to get a file's extension"""
        return os.path.splitext(filename)[1].lstrip(".")

    clips = []

    print(f"Paths = {len(audio_clip_paths[0])}")
    print(f"Paths = {len(audio_clip_paths[1])}")
    #print(f"Paths = {len(audio_clip_paths)}")
    print(f"Paths = {type(audio_clip_paths)}")

    for p in audio_clip_paths:
        print(f"ACP - {p}")

    # wrap the audio clip paths with tqdm if verbose
    audio_clip_paths = tqdm(audio_clip_paths, "Reading audio file") if verbose else audio_clip_paths
    for clip_path in list(audio_clip_paths):
        # get extension of the audio file
        extension = get_file_extension(clip_path)
        # load the audio clip and append it to our list
        clip = AudioSegment.from_file(clip_path, extension)
        clips.append(clip)

    final_clip = clips[0]
    range_loop = tqdm(list(range(1, len(clips))), "Concatenating audio") if verbose else range(1, len(clips))
    for i in range_loop:
        # looping on all audio files and concatenating them together
        # ofc order is important
        final_clip = final_clip + clips[i]
    # export the final clip
    final_clip_extension = get_file_extension(output_path)
    if verbose:
        print(f"Exporting resulting audio file to {output_path}")
    final_clip.export(output_path, format=final_clip_extension)