package com.example.eunoia.dashboard.upload_files

import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.os.Environment
import android.os.Process
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.math.roundToInt

fun convertAudioToAAC(audioFileName: String, compressedAudioFileName: String){
    val AUDIO_RECORDING_FILE_NAME = audioFileName //"audio_Capturing-190814-034638.422.wav" // Input PCM file
    val COMPRESSED_AUDIO_FILE_NAME = compressedAudioFileName //"convertedmp4.m4a" // Output MP4/M4A file
    val COMPRESSED_AUDIO_FILE_MIME_TYPE = "audio/aac"
    val COMPRESSED_AUDIO_FILE_BIT_RATE = 64000 // 64kbps
    val SAMPLING_RATE = 48000
    val BUFFER_SIZE = 48000
    val CODEC_TIMEOUT_IN_MS = 5000
    val LOGTAG = "CONVERT AUDIO"
    var convert = Runnable {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
        try {
            val filePath: String = Environment.getExternalStorageDirectory().path
                .toString() + "/" + AUDIO_RECORDING_FILE_NAME
            val inputFile = File(filePath)
            val fis = FileInputStream(inputFile)
            val outputFile = File(
                Environment.getExternalStorageDirectory().absolutePath
                    .toString() + "/" + COMPRESSED_AUDIO_FILE_NAME
            )
            if (outputFile.exists()) outputFile.delete()
            val mux =
                MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            var outputFormat =
                MediaFormat.createAudioFormat(COMPRESSED_AUDIO_FILE_MIME_TYPE, SAMPLING_RATE, 1)
            outputFormat.setInteger(
                MediaFormat.KEY_AAC_PROFILE,
                MediaCodecInfo.CodecProfileLevel.AACObjectLC
            )
            outputFormat.setInteger(MediaFormat.KEY_BIT_RATE, COMPRESSED_AUDIO_FILE_BIT_RATE)
            outputFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 16384)
            val codec = MediaCodec.createEncoderByType(COMPRESSED_AUDIO_FILE_MIME_TYPE)
            codec.configure(outputFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            codec.start()
            val codecInputBuffers: Array<ByteBuffer> = codec.inputBuffers // Note: Array of buffers
            val codecOutputBuffers: Array<ByteBuffer> = codec.outputBuffers
            val outBuffInfo = MediaCodec.BufferInfo()
            val tempBuffer = ByteArray(BUFFER_SIZE)
            var hasMoreData = true
            var presentationTimeUs = 0.0
            var audioTrackIdx = 0
            var totalBytesRead = 0
            var percentComplete: Int
            do {
                var inputBufIndex = 0
                while (inputBufIndex != -1 && hasMoreData) {
                    inputBufIndex = codec.dequeueInputBuffer(CODEC_TIMEOUT_IN_MS.toLong())
                    if (inputBufIndex >= 0) {
                        val dstBuf: ByteBuffer = codecInputBuffers[inputBufIndex]
                        dstBuf.clear()
                        val bytesRead: Int = fis.read(tempBuffer, 0, dstBuf.limit())
                        Log.e("bytesRead", "Readed $bytesRead")
                        if (bytesRead == -1) { // -1 implies EOS
                            hasMoreData = false
                            codec.queueInputBuffer(
                                inputBufIndex,
                                0,
                                0,
                                presentationTimeUs.toLong(),
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM
                            )
                        } else {
                            totalBytesRead += bytesRead
                            dstBuf.put(tempBuffer, 0, bytesRead)
                            codec.queueInputBuffer(
                                inputBufIndex,
                                0,
                                bytesRead,
                                presentationTimeUs.toLong(),
                                0
                            )
                            presentationTimeUs =
                                (1000000L * (totalBytesRead / 2) / SAMPLING_RATE).toDouble()
                        }
                    }
                }
                // Drain audio
                var outputBufIndex = 0
                while (outputBufIndex != MediaCodec.INFO_TRY_AGAIN_LATER) {
                    outputBufIndex =
                        codec.dequeueOutputBuffer(outBuffInfo, CODEC_TIMEOUT_IN_MS.toLong())
                    if (outputBufIndex >= 0) {
                        val encodedData: ByteBuffer = codecOutputBuffers[outputBufIndex]
                        encodedData.position(outBuffInfo.offset)
                        encodedData.limit(outBuffInfo.offset + outBuffInfo.size)
                        if (outBuffInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0 && outBuffInfo.size != 0) {
                            codec.releaseOutputBuffer(outputBufIndex, false)
                        } else {
                            mux.writeSampleData(
                                audioTrackIdx,
                                codecOutputBuffers[outputBufIndex], outBuffInfo
                            )
                            codec.releaseOutputBuffer(outputBufIndex, false)
                        }
                    } else if (outputBufIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                        outputFormat = codec.outputFormat
                        Log.v(LOGTAG, "Output format changed - $outputFormat")
                        audioTrackIdx = mux.addTrack(outputFormat)
                        mux.start()
                    } else if (outputBufIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                        Log.e(LOGTAG, "Output buffers changed during encode!")
                    } else if (outputBufIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                        // NO OP
                    } else {
                        Log.e(
                            LOGTAG,
                            "Unknown return code from dequeueOutputBuffer - $outputBufIndex"
                        )
                    }
                }
                percentComplete =
                    (totalBytesRead.toFloat() / inputFile.length().toFloat() * 100.0).roundToInt()
                Log.v(LOGTAG, "Conversion % - $percentComplete")
            } while (outBuffInfo.flags != MediaCodec.BUFFER_FLAG_END_OF_STREAM)
            fis.close()
            mux.stop()
            mux.release()
            Log.v(LOGTAG, "Compression done ...")
        } catch (e: FileNotFoundException) {
            Log.e(LOGTAG, "File not found!", e)
        } catch (e: IOException) {
            Log.e(LOGTAG, "IO exception!", e)
        }

        //mStop = false;
        // Notify UI thread...
    }
}